package uz.script.wincrm.suppliers.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.suppliers.dto.SupplierDTO;
import uz.script.wincrm.suppliers.dto.SupplierFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierResponse;
import uz.script.wincrm.utils.Status;

public interface SupplierService {

    /**
     * Create new supplier
     */
    SupplierResponse create(SupplierDTO dto);

    /**
     * Update supplier
     */
    SupplierResponse update(Long id, SupplierDTO dto);

    /**
     * Delete supplier (Soft Delete)
     */
    void delete(Long id);

    /**
     * Get supplier by id
     */
    SupplierResponse findById(Long id);

    /**
     * Get all suppliers
     */
    Page<SupplierResponse> findAll(Pageable pageable);

    /**
     * Search & Filter suppliers
     */
    Page<SupplierResponse> filter(SupplierFilterDTO filter, Pageable pageable);

    /**
     * Change supplier status
     */
    SupplierResponse changeStatus(Long id, Status status);
}