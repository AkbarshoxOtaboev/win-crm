package uz.script.wincrm.suppliers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.script.wincrm.suppliers.SupplierPayment;

public interface SupplierPaymentRepository extends
        JpaRepository<SupplierPayment, Long>,
        JpaSpecificationExecutor<SupplierPayment> {
}