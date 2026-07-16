package uz.script.wincrm.suppliers.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.suppliers.dto.SupplierPaymentDTO;
import uz.script.wincrm.suppliers.dto.SupplierPaymentFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierPaymentResponse;

public interface SupplierPaymentService {

    /**
     * Create new supplier payment (balansni ham yangilaydi)
     */
    SupplierPaymentResponse create(SupplierPaymentDTO dto);

    /**
     * Update supplier payment (summa farqi bo'yicha balansni to'g'rilaydi)
     */
    SupplierPaymentResponse update(Long id, SupplierPaymentDTO dto);

    /**
     * Delete supplier payment (Soft Delete, balansni qaytaradi)
     */
    void delete(Long id);

    /**
     * Get supplier payment by id
     */
    SupplierPaymentResponse findById(Long id);

    /**
     * Get all supplier payments
     */
    Page<SupplierPaymentResponse> findAll(Pageable pageable);

    /**
     * Search & Filter supplier payments
     */
    Page<SupplierPaymentResponse> filter(SupplierPaymentFilterDTO filter, Pageable pageable);
}