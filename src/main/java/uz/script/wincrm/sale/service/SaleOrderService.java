package uz.script.wincrm.sale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.sale.dto.ApplyDiscountDTO;
import uz.script.wincrm.sale.dto.SaleOrderDTO;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.sale.response.SaleOrderDiscountHistoryResponse;
import uz.script.wincrm.sale.response.SaleOrderResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleOrderService {

    SaleOrderResponse create(SaleOrderDTO dto);

    SaleOrderResponse findById(Long id);

    Page<SaleOrderResponse> fetchAll(Pageable pageable);

    Page<SaleOrderResponse> fetchByClientId(Long clientId, Pageable pageable);

    Page<SaleOrderResponse> fetchByWarehouseId(Long warehouseId, Pageable pageable);

    Page<SaleOrderResponse> fetchByUserId(Long userId, Pageable pageable);

    List<SaleOrderResponse> fetchByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    SaleOrderResponse update(Long id, SaleOrderDTO dto);

    void delete(Long id);

    void changeStatus(Long id, SalesOrderStatus salesOrderStatus);

    /**
     * Buyurtmaga chegirma qo'llaydi. Faqat totalSum'ga ta'sir qiladi:
     * totalSum = originalTotalSum - discountAmount. Yakuniy holatda buyurtma
     * COMPLETED yoki CANCELLED bo'lmagan bo'lishi kerak.
     */
    SaleOrderResponse applyDiscount(Long id, ApplyDiscountDTO dto);

    /**
     * Buyurtmaga qo'llangan chegirmalar tarixini xronologik qaytaradi.
     */
    List<SaleOrderDiscountHistoryResponse> fetchDiscountHistory(Long id);
}