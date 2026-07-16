package uz.script.wincrm.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.repository.SupplierRepository;
import uz.script.wincrm.suppliers.service.SupplierBalanceService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.WarehouseOrder;
import uz.script.wincrm.warehouse.WarehouseOrderItem;
import uz.script.wincrm.warehouse.dto.WarehouseOrderItemDTO;
import uz.script.wincrm.warehouse.mapper.WarehouseOrderItemMapper;
import uz.script.wincrm.warehouse.repository.WarehouseOrderItemRepository;
import uz.script.wincrm.warehouse.repository.WarehouseOrderRepository;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;
import uz.script.wincrm.warehouse.response.WarehouseOrderItemResponse;
import uz.script.wincrm.warehouse.service.WarehouseOrderItemService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WarehouseOrderItemServiceImpl implements WarehouseOrderItemService {

    private final WarehouseOrderItemRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseOrderRepository warehouseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final GoodsRepository goodsRepository;
    private final WarehouseOrderItemMapper mapper;
    private final StockService stockService;
    private final SupplierBalanceService supplierBalanceService;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "WarehouseOrderItem"
    )
//    @Caching(evict = {
//            @CacheEvict(value = "warehouseOrderItems", allEntries = true),
//            @CacheEvict(value = {"warehouseOrders", "warehouseOrder"}, allEntries = true)
//    })
    @Transactional
    public WarehouseOrderItemResponse create(WarehouseOrderItemDTO dto) {
        log.info("Create warehouse order item");

        Warehouse warehouse = findWarehouse(dto.getWarehouseId());
        WarehouseOrder warehouseOrder = findWarehouseOrder(dto.getWarehouseOrderId());
        Supplier supplier = findSupplier(dto.getSupplierId());
        Goods goods = findGoods(dto.getGoodsId());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        WarehouseOrderItem item = mapper.toEntity(dto, warehouse, warehouseOrder, supplier, goods);
        item.setCreatedUsername(username);

        item = repository.save(item);
        // Omborga mahsulot kirim qilindi -> Stockga yozamiz (Stock + StockHistory avtomatik)
        stockService.increaseStock(dto.getGoodsId(), dto.getWarehouseId(), dto.getCount());
        recalculateOrderTotalSum(warehouseOrder);

        return mapper.toResponse(item);
    }

    @Override
//    @Cacheable(value = "warehouseOrderItem", key = "#id")
    public WarehouseOrderItemResponse findById(Long id) {
        log.info("Fetch warehouse order item by id {}", id);

        WarehouseOrderItem item = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse order item not found with id: " + id));

        return mapper.toResponse(item);
    }

    @Override
//    @Cacheable(value = "warehouseOrderItems")
    public List<WarehouseOrderItemResponse> fetchAllItems() {
        log.info("Fetch all warehouse order items");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
//    @Cacheable(value = "warehouseOrderItemsByOrder", key = "#warehouseOrderId")
    public List<WarehouseOrderItemResponse> fetchByWarehouseOrderId(Long warehouseOrderId) {
        log.info("Fetch warehouse order items by order id {}", warehouseOrderId);

        return repository.findAllByWarehouseOrderId(warehouseOrderId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<WarehouseOrderItemResponse> fetchByGoodsId(Long goodsId) {
        log.info("Fetch warehouse order items by goods id {}", goodsId);

        return repository.findAllByGoodsId(goodsId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "WarehouseOrderItem"
    )
//    @Caching(evict = {
//            @CacheEvict(value = {"warehouseOrderItems", "warehouseOrderItem", "warehouseOrderItemsByOrder"}, allEntries = true),
//            @CacheEvict(value = {"warehouseOrders", "warehouseOrder"}, allEntries = true)
//    })
    @Transactional
    public WarehouseOrderItemResponse update(Long id, WarehouseOrderItemDTO dto) {
        log.info("Update warehouse order item with id {}", id);

        WarehouseOrderItem item = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse order item not found with id: " + id));

        Warehouse warehouse = findWarehouse(dto.getWarehouseId());
        WarehouseOrder warehouseOrder = findWarehouseOrder(dto.getWarehouseOrderId());
        Supplier supplier = findSupplier(dto.getSupplierId());
        Goods goods = findGoods(dto.getGoodsId());

        WarehouseOrder previousOrder = item.getWarehouseOrder();

        // Stockni to'g'irlash uchun ESKI qiymatlarni mapper ustidan yozib yuborishidan oldin saqlab qolamiz
        Long previousGoodsId = item.getGoods().getId();
        Long previousWarehouseId = item.getWarehouse().getId();
        BigDecimal previousCount = item.getCount();

        mapper.updateEntity(item, dto, warehouse, warehouseOrder, supplier, goods);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        item.setCreatedUsername(username);
        item = repository.save(item);

        // Eski goods/warehouse bo'yicha qo'shilgan miqdorni Stockdan ayiramiz (chiqim)
        stockService.decreaseStock(previousGoodsId, previousWarehouseId, previousCount);
        // Yangi goods/warehouse/count bo'yicha qayta Stockga qo'shamiz (kirim)
        stockService.increaseStock(dto.getGoodsId(), dto.getWarehouseId(), dto.getCount());

        recalculateOrderTotalSum(warehouseOrder);
        if (previousOrder != null && !previousOrder.getId().equals(warehouseOrder.getId())) {
            recalculateOrderTotalSum(previousOrder);
        }

        return mapper.toResponse(item);
    }
    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "WarehouseOrderItem"
    )
//    @Caching(evict = {
//            @CacheEvict(value = {"warehouseOrderItems", "warehouseOrderItem", "warehouseOrderItemsByOrder"}, allEntries = true),
//            @CacheEvict(value = {"warehouseOrders", "warehouseOrder"}, allEntries = true)
//    })
    public void delete(Long id) {
        log.info("Delete warehouse order item with id {}", id);

        WarehouseOrderItem item = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse order item not found with id: " + id));

        item.setStatus(Status.DELETED);
        repository.save(item);

        recalculateOrderTotalSum(item.getWarehouseOrder());
    }

    /**
     * WarehouseOrder.totalSum ni faol item'lar asosida qayta hisoblaydi va
     * eski/yangi totalSum orasidagi farqni supplier balansiga (totalPurchase/totalDebt)
     * qo'llaydi. Item qo'shilishi, o'chirilishi yoki tahrirlanishi natijasida
     * totalSum o'zgargan barcha holatlar shu yerdan o'tadi.
     */
    private void recalculateOrderTotalSum(WarehouseOrder warehouseOrder) {
        List<WarehouseOrderItem> items = repository.findAllByWarehouseOrderId(warehouseOrder.getId());

        BigDecimal newTotal = items.stream()
                .filter(i -> i.getStatus() == Status.ACTIVE)
                .map(i -> i.getPriceSelling().multiply(i.getCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal oldTotal = warehouseOrder.getTotalSum() != null
                ? warehouseOrder.getTotalSum()
                : BigDecimal.ZERO;

        warehouseOrder.setTotalSum(newTotal);
        warehouseOrderRepository.save(warehouseOrder);

        BigDecimal diff = newTotal.subtract(oldTotal);
        Long supplierId = warehouseOrder.getSupplier().getId();

        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            supplierBalanceService.increasePurchase(supplierId, diff);
            log.info("Supplier balance increased by purchase diff. SupplierId: {}, Diff: {}",
                    supplierId, diff);
        } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
            supplierBalanceService.decreasePurchase(supplierId, diff.abs());
            log.info("Supplier balance decreased by purchase diff. SupplierId: {}, Diff: {}",
                    supplierId, diff.abs());
        }
    }

    private Warehouse findWarehouse(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }

    private WarehouseOrder findWarehouseOrder(Long id) {
        return warehouseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse order not found with id: " + id));
    }

    private Supplier findSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }

    private Goods findGoods(Long id) {
        return goodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods not found with id: " + id));
    }
}