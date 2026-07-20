package uz.script.wincrm.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.repository.SupplierRepository;
import uz.script.wincrm.suppliers.service.SupplierBalanceService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.WarehouseOrder;
import uz.script.wincrm.warehouse.WarehouseOrderItem;
import uz.script.wincrm.warehouse.dto.WarehouseOrderDTO;
import uz.script.wincrm.warehouse.enums.WarehouseOrderStatus;
import uz.script.wincrm.warehouse.mapper.WarehouseOrderMapper;
import uz.script.wincrm.warehouse.repository.WarehouseOrderItemRepository;
import uz.script.wincrm.warehouse.repository.WarehouseOrderRepository;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;
import uz.script.wincrm.warehouse.response.WarehouseOrderResponse;
import uz.script.wincrm.warehouse.service.WarehouseOrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WarehouseOrderServiceImpl implements WarehouseOrderService {

    private final WarehouseOrderRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseOrderMapper mapper;
    private final SupplierBalanceService supplierBalanceService;
    private final WarehouseOrderItemRepository warehouseOrderItemRepository;
    private final StockService stockService;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "WarehouseOrder"
    )
//    @CacheEvict(value = "warehouseOrders", allEntries = true)
    public WarehouseOrderResponse create(WarehouseOrderDTO dto) {
        log.info("Create warehouse order");

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        WarehouseOrder order = mapper.toEntity(dto, supplier, warehouse);
        order.setCreatedUsername(username);

        // Order yaratilganda hali item yo'q, totalSum = 0/null.
        // Supplier balansi (totalPurchase/totalDebt) faqat item qo'shilganda
        // WarehouseOrderItemServiceImpl#recalculateOrderTotalSum orqali yangilanadi.
        order = repository.save(order);

        return mapper.toResponse(order);
    }

    @Override
//    @Cacheable(value = "warehouseOrder", key = "#id")
    public WarehouseOrderResponse findById(Long id) {
        log.info("Fetch warehouse order by id {}", id);

        WarehouseOrder order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse order not found with id: " + id));

        return mapper.toResponse(order);
    }

    @Override
//    @Cacheable(value = "warehouseOrders")
    public List<WarehouseOrderResponse> fetchAllOrders() {
        log.info("Fetch all warehouse orders");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
//    @Cacheable(value = "warehouseOrdersByWarehouse", key = "#warehouseId")
    public List<WarehouseOrderResponse> fetchByWarehouseId(Long warehouseId) {
        log.info("Fetch warehouse orders by warehouse id {}", warehouseId);

        return repository.findAllByWarehouseId(warehouseId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
//    @Cacheable(value = "warehouseOrdersBySupplier", key = "#supplierId")
    public List<WarehouseOrderResponse> fetchBySupplierId(Long supplierId) {
        log.info("Fetch warehouse orders by supplier id {}", supplierId);

        return repository.findAllBySupplierId(supplierId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "WarehouseOrder"
    )
//    @CacheEvict(value = {"warehouseOrders", "warehouseOrder", "warehouseOrdersByWarehouse", "warehouseOrdersBySupplier"}, allEntries = true)
    public WarehouseOrderResponse update(Long id, WarehouseOrderDTO dto) {
        log.info("Update warehouse order with id {}", id);

        WarehouseOrder order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse order not found with id: " + id));

        Supplier newSupplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        // WarehouseOrderDTO totalSum saqlamaydi (u item'lardan avtomatik hisoblanadi),
        // shuning uchun bu yerda faqat supplier o'zgarganda balansni ko'chiramiz.
        Long oldSupplierId = order.getSupplier().getId();
        Long newSupplierId = newSupplier.getId();
        BigDecimal currentTotalSum = order.getTotalSum() != null ? order.getTotalSum() : BigDecimal.ZERO;

        mapper.updateEntity(order, dto, newSupplier, warehouse);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setCreatedUsername(username);
        order = repository.save(order);

        if (!Objects.equals(oldSupplierId, newSupplierId)
                && currentTotalSum.compareTo(BigDecimal.ZERO) != 0) {

            supplierBalanceService.decreasePurchase(oldSupplierId, currentTotalSum);
            supplierBalanceService.increasePurchase(newSupplierId, currentTotalSum);

            log.info("Order's supplier changed, balance transferred. Old supplierId: {} (-{}), New supplierId: {} (+{})",
                    oldSupplierId, currentTotalSum, newSupplierId, currentTotalSum);
        }

        return mapper.toResponse(order);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "WarehouseOrder"
    )
//    @CacheEvict(value = {"warehouseOrders", "warehouseOrder", "warehouseOrdersByWarehouse", "warehouseOrdersBySupplier"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete warehouse order with id {}", id);

        WarehouseOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse order not found with id: " + id));

        if (order.getOrderStatus() == WarehouseOrderStatus.TRANSFERRED) {
            List<WarehouseOrderItem> items = warehouseOrderItemRepository.findAllByWarehouseOrderId(id);
            for (WarehouseOrderItem item : items) {
                if (item.getStatus() == Status.ACTIVE) {
                    stockService.decreaseStock(item.getGoods().getId(), item.getWarehouse().getId(), item.getCount());
                }
            }
            log.info("Order was TRANSFERRED, stock reverted for {} items", items.size());
        }

        order.setStatus(Status.DELETED);
        repository.save(order);

        BigDecimal totalSum = order.getTotalSum() != null ? order.getTotalSum() : BigDecimal.ZERO;
        if (totalSum.compareTo(BigDecimal.ZERO) != 0) {
            supplierBalanceService.decreasePurchase(order.getSupplier().getId(), totalSum);
            log.info("Supplier balance decreased by purchase. SupplierId: {}, Sum: {}",
                    order.getSupplier().getId(), totalSum);
        }
    }

    @Override
    @Auditable(action = AuditAction.UPDATE, entity = "WarehouseOrder")
    public WarehouseOrderResponse transferToWarehouse(Long id) {
        log.info("Transfer warehouse order to stock, id {}", id);

        WarehouseOrder order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse order not found with id: " + id));

        if (order.getOrderStatus() == WarehouseOrderStatus.TRANSFERRED) {
            throw new IllegalStateException("Order allaqachon omborga transfer qilingan: " + id);
        }

        List<WarehouseOrderItem> items = warehouseOrderItemRepository.findAllByWarehouseOrderId(id)
                .stream()
                .filter(i -> i.getStatus() == Status.ACTIVE)
                .toList();

        if (items.isEmpty()) {
            throw new IllegalStateException("Item'lari yo'q orderni transfer qilib bo'lmaydi: " + id);
        }

        for (WarehouseOrderItem item : items) {
            stockService.increaseStock(item.getGoods().getId(), item.getWarehouse().getId(), item.getCount());
        }

        order.setOrderStatus(WarehouseOrderStatus.TRANSFERRED);
        order = repository.save(order);

        log.info("Order transferred to stock. OrderId: {}, items: {}", id, items.size());
        return mapper.toResponse(order);
    }
}