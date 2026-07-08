package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.SaleOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {

    Page<SaleOrder> findByClientId(Long clientId, Pageable pageable);

    Page<SaleOrder> findByWarehouseId(Long warehouseId, Pageable pageable);

    List<SaleOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT so FROM SaleOrder so WHERE so.client.id = :clientId AND so.warehouse.id = :warehouseId")
    Page<SaleOrder> findByClientIdAndWarehouseId(
            @Param("clientId") Long clientId,
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );

    Optional<SaleOrder> findByIdAndWarehouseId(Long id, Long warehouseId);

//    Optional<SaleOrder> findByIdAndStatusNotDeleted(Long id);
}