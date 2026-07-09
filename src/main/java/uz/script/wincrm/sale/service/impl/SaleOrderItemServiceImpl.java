package uz.script.wincrm.sale.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.clients.ClientRepository;
import uz.script.wincrm.exceptions.InsufficientStockException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.enums.Type;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderItem;
import uz.script.wincrm.sale.dto.SaleOrderItemDTO;
import uz.script.wincrm.sale.mapper.SaleOrderItemMapper;
import uz.script.wincrm.sale.repository.SaleOrderItemRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.response.SaleOrderItemResponse;
import uz.script.wincrm.sale.service.SaleOrderItemService;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.utils.PeriodType;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SaleOrderItemServiceImpl implements SaleOrderItemService {

    private final SaleOrderItemRepository repository;
    private final SaleOrderItemMapper mapper;
    private final SaleOrderRepository saleOrderRepository;
    private final ClientRepository clientRepository;
    private final GoodsRepository goodsRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockService stockService;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "SaleOrderItem"
    )
//    @CacheEvict(value = "saleOrderItems", allEntries = true)
    @Transactional
    public SaleOrderItemResponse create(SaleOrderItemDTO dto) {
        log.info("Create sale order item. Goods ID: {}, Warehouse ID: {}, Requested count: {}",
                dto.getGoodsId(), dto.getWarehouseId(), dto.getCount());

        // ⭐ WAREHOUSE STOCK VALIDATION - MUHIM!
        validateAndCheckStock(dto.getWarehouseId(), dto.getGoodsId(), dto.getCount());

        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Goods not found with id: " + dto.getGoodsId()));

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));

        SaleOrder saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + dto.getSaleOrderId()));


        SaleOrderItem entity = mapper.toEntity(dto);
        entity.setGoods(goods);
        entity.setWarehouse(warehouse);
        entity.setClient(client);
        entity.setSaleOrder(saleOrder);
        entity.setStatus(Status.ACTIVE);

        entity = repository.save(entity);
        // Ombordan mahsulot sotildi -> Stockdan chiqim qilamiz (Stock + StockHistory OUT avtomatik)
        stockService.decreaseStock(dto.getGoodsId(), dto.getWarehouseId(), dto.getCount());
        log.info("Sale order item created successfully. ID: {}", entity.getId());

        return mapper.toResponse(entity);
    }

    @Override
//    @Cacheable(value = "saleOrderItem", key = "#id")
    public SaleOrderItemResponse findById(Long id) {
        log.info("Fetch sale order item by id {}", id);

        SaleOrderItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order item not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
//    @Cacheable(value = "saleOrderItems")
    public Page<SaleOrderItemResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all sale order items");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<SaleOrderItemResponse> fetchBySaleOrderId(Long saleOrderId) {
        log.info("Fetch sale order items by sale order id {}", saleOrderId);

        return repository.findAllBySaleOrderId(saleOrderId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Page<SaleOrderItemResponse> fetchBySaleOrderIdPaginated(Long saleOrderId, Pageable pageable) {
        log.info("Fetch sale order items paginated by sale order id {}", saleOrderId);

        return repository.findAllBySaleOrderId(saleOrderId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderItemResponse> fetchByClientId(Long clientId, Pageable pageable) {
        log.info("Fetch sale order items by client id {}", clientId);

        return repository.findByClientId(clientId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderItemResponse> fetchByWarehouseId(Long warehouseId, Pageable pageable) {
        log.info("Fetch sale order items by warehouse id {}", warehouseId);

        return repository.findByWarehouseId(warehouseId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderItemResponse> fetchByGoodsId(Long goodsId, Pageable pageable) {
        log.info("Fetch sale order items by goods id {}", goodsId);

        return repository.findByGoodsId(goodsId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<SaleOrderItemResponse> fetchByArrivalDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetch sale order items by arrival date range {} - {}", startDate, endDate);

        return repository.findByArrivalDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "SaleOrderItem"
    )
//    @CacheEvict(value = {"saleOrderItems", "saleOrderItem"}, allEntries = true)
    @Transactional
    public SaleOrderItemResponse update(Long id, SaleOrderItemDTO dto) {
        log.info("Update sale order item with id {}", id);

        SaleOrderItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order item not found with id: " + id));

        BigDecimal oldCount = entity.getCount();
        Long goodsId = entity.getGoods().getId();
        Long warehouseId = entity.getWarehouse().getId();

        // Count o'zgargansa: avval eski miqdorni Stockga qaytaramiz,
        // shundagina Stock haqiqiy (bo'sh) holatni ko'rsatadi va validatsiya to'g'ri ishlaydi
        if (!oldCount.equals(dto.getCount())) {
            log.info("Count changed from {} to {}. Returning old count to stock before validating...",
                    oldCount, dto.getCount());

            stockService.increaseStock(goodsId, warehouseId, oldCount);

            validateAndCheckStock(warehouseId, goodsId, dto.getCount());
        }

        mapper.updateEntity(entity, dto);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        entity.setCreatedUsername(username);

        entity = repository.save(entity);

        // Count o'zgargan bo'lsa, yangi miqdorni Stockdan qayta chiqim qilamiz
        if (!oldCount.equals(dto.getCount())) {
            stockService.decreaseStock(goodsId, warehouseId, dto.getCount());
        }

        return mapper.toResponse(entity);
    }
    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "SaleOrderItem"
    )
//    @CacheEvict(value = {"saleOrderItems", "saleOrderItem"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete sale order item with id {}", id);

        SaleOrderItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order item not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    /**
     * ⭐ WAREHOUSE STOCK VALIDATION METODI ⭐
     * Bu metod warehouse'dagi mahsulot stokunni tekshiradi
     * Agar warehouse'da 10 bor leki client 12 ta kirgizmonga harakat qilsa - XATOLIK!
     *
     * @throws InsufficientStockException agar stock yetarli bo'lmasa
     */
    @Override
    public void validateAndCheckStock(Long warehouseId, Long goodsId, BigDecimal requestedCount) {
        log.info("Validating warehouse stock. Warehouse: {}, Goods: {}, Requested: {}",
                warehouseId, goodsId, requestedCount);

        // Manfiy yoki nol qiymat check
        if (requestedCount == null || requestedCount.compareTo(BigDecimal.ZERO) <= 0) {
            String errorMsg = String.format(
                    "Mahsulot soni nol'dan katta bo'lishi kerak! Kirgizilgan: %s",
                    requestedCount
            );
            log.error(errorMsg);
            throw new InsufficientStockException(errorMsg);
        }

        // Warehouse'dagi mavjud stockni Stock jadvalidan (haqiqiy manba) olamiz
        BigDecimal availableStock = stockService.getAvailableStock(goodsId, warehouseId);

        log.info("Stock check: available={}, requested={}", availableStock, requestedCount);

        // Agar mavjud stock kirgizilgan sonidan kam bo'lsa - XATOLIK!
        if (availableStock.compareTo(requestedCount) < 0) {
            BigDecimal shortage = requestedCount.subtract(availableStock);
            String errorMsg = String.format(
                    "Ombareda yetarli mahsulot yo'q! Mavjud: %s, Talabalar: %s, Kamiy: %s",
                    availableStock,
                    requestedCount,
                    shortage
            );
            log.error(errorMsg);
            throw new InsufficientStockException(
                    errorMsg,
                    goodsId,
                    warehouseId,
                    availableStock,
                    requestedCount
            );
        }

        log.info("Stock validation passed successfully");
    }


    @Override
    public Page<SaleOrderItemResponse> fetchByGoodsTypeAndPeriod(Type type, PeriodType period, Pageable pageable) {
        log.info("Filter sale order items by goods type {} and period {}", type, period);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = resolvePeriodStart(period, now);

        return repository.findByGoodsTypeAndArrivalDateBetween(type, startDate, now, pageable)
                .map(mapper::toResponse);
    }

    /**
     * DAILY / WEEKLY / MONTHLY davr uchun boshlanish sanasini hisoblaydi.
     * DAILY   -> bugungi kun 00:00
     * WEEKLY  -> shu haftaning dushanbasi 00:00
     * MONTHLY -> shu oyning 1-kuni 00:00
     */
    private LocalDateTime resolvePeriodStart(PeriodType period, LocalDateTime now) {
        return switch (period) {
            case DAILY -> now.toLocalDate().atStartOfDay();
            case WEEKLY -> now.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay();
            case MONTHLY -> now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        };
    }
}