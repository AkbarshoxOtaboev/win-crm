package uz.script.wincrm.inventory.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.inventory.InventoryCheck;
import uz.script.wincrm.inventory.InventoryCheckItem;
import uz.script.wincrm.inventory.enums.InventoryCheckStatus;
import uz.script.wincrm.inventory.mapper.InventoryCheckMapper;
import uz.script.wincrm.inventory.repository.InventoryCheckItemRepository;
import uz.script.wincrm.inventory.repository.InventoryCheckRepository;
import uz.script.wincrm.inventory.request.StartInventoryCheckRequest;
import uz.script.wincrm.inventory.request.UpdateInventoryCheckItemRequest;
import uz.script.wincrm.inventory.response.InventoryCheckResponse;
import uz.script.wincrm.inventory.service.InventoryCheckService;
import uz.script.wincrm.security.CustomUserDetails;
import uz.script.wincrm.stock.Stock;
import uz.script.wincrm.stock.repository.StockRepository;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryCheckServiceImpl implements InventoryCheckService {

    private final InventoryCheckRepository inventoryCheckRepository;
    private final InventoryCheckItemRepository inventoryCheckItemRepository;
    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockService stockService;
    private final InventoryCheckMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public InventoryCheckResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCheckResponse> fetchAll() {
        return inventoryCheckRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCheckResponse> fetchByWarehouseId(Long warehouseId) {
        return inventoryCheckRepository.findAllByWarehouseId(warehouseId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryCheckResponse start(StartInventoryCheckRequest request) {
        inventoryCheckRepository.findByWarehouseIdAndCheckStatus(request.getWarehouseId(), InventoryCheckStatus.IN_PROGRESS)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Bu ombor uchun allaqachon tugallanmagan (IN_PROGRESS) inventarizatsiya mavjud, id: " + existing.getId());
                });

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        List<Stock> stocks = stockRepository.findAllByWarehouseId(request.getWarehouseId());

        InventoryCheck inventoryCheck = InventoryCheck.builder()
                .warehouse(warehouse)
                .checkStatus(InventoryCheckStatus.IN_PROGRESS)
                .comment(request.getComment())
                .build();

        stocks.forEach(stock -> {
            InventoryCheckItem item = InventoryCheckItem.builder()
                    .inventoryCheck(inventoryCheck)
                    .goods(stock.getGoods())
                    .systemCount(stock.getCount())
                    .build();
            inventoryCheck.getItems().add(item);
        });

        InventoryCheck saved = inventoryCheckRepository.save(inventoryCheck);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryCheckResponse updateItem(Long inventoryCheckId, Long itemId, UpdateInventoryCheckItemRequest request) {
        InventoryCheck inventoryCheck = getOrThrow(inventoryCheckId);
        validateInProgress(inventoryCheck);

        InventoryCheckItem item = inventoryCheckItemRepository.findByIdAndInventoryCheckId(itemId, inventoryCheckId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Inventory check item not found with id: " + itemId + " in inventoryCheckId: " + inventoryCheckId));

        item.setActualCount(request.getActualCount());
        item.setDifference(request.getActualCount().subtract(item.getSystemCount()));
        item.setComment(request.getComment());
        inventoryCheckItemRepository.save(item);

        return mapper.toResponse(getOrThrow(inventoryCheckId));
    }

    @Override
    @Transactional
    public InventoryCheckResponse confirm(Long inventoryCheckId) {
        InventoryCheck inventoryCheck = getOrThrow(inventoryCheckId);
        validateInProgress(inventoryCheck);

        List<InventoryCheckItem> items = inventoryCheckItemRepository.findAllByInventoryCheckId(inventoryCheckId);

        boolean hasUncounted = items.stream().anyMatch(item -> item.getActualCount() == null);
        if (hasUncounted) {
            throw new IllegalStateException("Tasdiqlashdan oldin barcha mahsulotlar sanalishi (actualCount kiritilishi) kerak");
        }

        Long warehouseId = inventoryCheck.getWarehouse().getId();

        items.stream()
                .filter(item -> item.getDifference().compareTo(BigDecimal.ZERO) != 0)
                .forEach(item -> {
                    Long goodsId = item.getGoods().getId();
                    BigDecimal diff = item.getDifference();

                    if (diff.compareTo(BigDecimal.ZERO) > 0) {
                        // Haqiqiy qoldiq tizimdagidan ko'p - ortiqcha topildi, Stockga qo'shiladi
                        stockService.increaseStock(goodsId, warehouseId, diff);
                    } else {
                        // Haqiqiy qoldiq tizimdagidan kam - kamomad, Stockdan ayiriladi
                        stockService.decreaseStock(goodsId, warehouseId, diff.abs());
                    }
                });

        inventoryCheck.setCheckStatus(InventoryCheckStatus.CONFIRMED);
        inventoryCheck.setConfirmedAt(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            inventoryCheck.setConfirmedUserId(userDetails.getId());
            inventoryCheck.setConfirmedUsername(userDetails.getUsername());
        }

        InventoryCheck saved = inventoryCheckRepository.save(inventoryCheck);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryCheckResponse cancel(Long inventoryCheckId) {
        InventoryCheck inventoryCheck = getOrThrow(inventoryCheckId);
        validateInProgress(inventoryCheck);

        inventoryCheck.setCheckStatus(InventoryCheckStatus.CANCELLED);
        InventoryCheck saved = inventoryCheckRepository.save(inventoryCheck);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InventoryCheck inventoryCheck = getOrThrow(id);
        inventoryCheck.setStatus(Status.DELETED);
        inventoryCheckRepository.save(inventoryCheck);
    }

    private InventoryCheck getOrThrow(Long id) {
        return inventoryCheckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory check not found with id: " + id));
    }

    private void validateInProgress(InventoryCheck inventoryCheck) {
        if (inventoryCheck.getCheckStatus() != InventoryCheckStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Bu amal faqat IN_PROGRESS holatidagi inventarizatsiya uchun ruxsat etilgan, joriy holat: "
                            + inventoryCheck.getCheckStatus());
        }
    }
}