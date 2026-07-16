package uz.script.wincrm.suppliers.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.suppliers.dto.SupplierBalanceFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierBalanceResponse;

import java.math.BigDecimal;

public interface SupplierBalanceService {

    /**
     * WarehouseOrder yaratilganda chaqiriladi (ichki metod)
     */
    void increasePurchase(Long supplierId, BigDecimal amount);

    /**
     * WarehouseOrder o'chirilganda/kamaytirilganda chaqiriladi (ichki metod)
     */
    void decreasePurchase(Long supplierId, BigDecimal amount);

    /**
     * SupplierPayment yaratilganda chaqiriladi (ichki metod)
     */
    void increasePayment(Long supplierId, BigDecimal amount);

    /**
     * SupplierPayment o'chirilganda/kamaytirilganda chaqiriladi (ichki metod)
     */
    void decreasePayment(Long supplierId, BigDecimal amount);

    /**
     * Bitta supplier balansini olish
     */
    SupplierBalanceResponse findBySupplierId(Long supplierId);

    /**
     * Barcha balanslarni sahifalab olish
     */
    Page<SupplierBalanceResponse> findAll(Pageable pageable);

    /**
     * Balanslarni filtrlash
     */
    Page<SupplierBalanceResponse> filter(SupplierBalanceFilterDTO filter, Pageable pageable);
}