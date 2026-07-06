package uz.script.wincrm.suppliers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>,
        JpaSpecificationExecutor<Supplier> {

    boolean existsByInn(String inn);

    boolean existsByPhone(String phone);

    Optional<Supplier> findByInn(String inn);

    Optional<Supplier> findByPhone(String phone);

    boolean existsByInnAndIdNot(String inn, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}