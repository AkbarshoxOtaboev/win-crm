package uz.script.wincrm.suppliers.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.suppliers.SupplierBalance;

import java.util.Optional;
@Repository
public interface SupplierBalanceRepository extends
        JpaRepository<SupplierBalance, Long>,
        JpaSpecificationExecutor<SupplierBalance> {

    Optional<SupplierBalance> findBySupplierId(Long supplierId);

    boolean existsBySupplierId(Long supplierId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sb from SupplierBalance sb where sb.supplier.id = :supplierId")
    Optional<SupplierBalance> findBySupplierIdForUpdate(@Param("supplierId") Long supplierId);
}