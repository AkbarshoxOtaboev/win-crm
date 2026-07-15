package uz.script.wincrm.sale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.exceptions.InsufficientStockException;
import uz.script.wincrm.goods.enums.Type;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.dto.SaleOrderItemDTO;
import uz.script.wincrm.sale.response.SaleOrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleOrderItemService {

    /**
     * ⭐ Warehouse stock validatsiyasi bilan yangi element yaratadi
     * Agar warehouse'da 10 items bo'lsa va siz 12 ta kirgizmonga harakat qilsangiz:
     * InsufficientStockException jo'natiladi va element yaratilmaydi!
     */
    SaleOrderItemResponse create(SaleOrderItemDTO dto);

    SaleOrderItemResponse findById(Long id);

    Page<SaleOrderItemResponse> fetchAll(Pageable pageable);

    List<SaleOrderItemResponse> fetchBySaleOrderId(Long saleOrderId);

    Page<SaleOrderItemResponse> fetchBySaleOrderIdPaginated(Long saleOrderId, Pageable pageable);

    Page<SaleOrderItemResponse> fetchByClientId(Long clientId, Pageable pageable);

    Page<SaleOrderItemResponse> fetchByWarehouseId(Long warehouseId, Pageable pageable);

    Page<SaleOrderItemResponse> fetchByGoodsId(Long goodsId, Pageable pageable);

    List<SaleOrderItemResponse> fetchByArrivalDateRange(LocalDateTime startDate, LocalDateTime endDate);

    SaleOrderItemResponse update(Long id, SaleOrderItemDTO dto);

    void delete(Long id);

    /**
     * Warehouse stockining yetarlililigini tekshiradi
     * @throws InsufficientStockException agar stock yetarli bo'lmasa
     */
    void validateAndCheckStock(Long warehouseId, Long goodsId, BigDecimal requestedCount);

    /**
     * Goods.type va berilgan sana oralig'i (startDate - endDate) bo'yicha filtrlaydi.
     */
    Page<SaleOrderItemResponse> fetchByGoodsTypeAndDateRange(
            Type type,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    void validateSaleOrderStatus(SaleOrder saleOrder);
}