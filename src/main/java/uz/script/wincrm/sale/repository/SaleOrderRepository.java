package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.SaleOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {

    Page<SaleOrder> findByClientId(Long clientId, Pageable pageable);

    Page<SaleOrder> findByWarehouseId(Long warehouseId, Pageable pageable);

    Page<SaleOrder> findByUserId(Long userId, Pageable pageable);

    List<SaleOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Qarzi bor (debtSum > berilgan qiymat) barcha buyurtmalarni topadi.
     * Odatda BigDecimal.ZERO bilan chaqiriladi — qarzi bo'lgan buyurtmalarni olish uchun.
     */
    List<SaleOrder> findByDebtSumGreaterThan(BigDecimal amount);

    /**
     * Bitta mijozning qarzi bor buyurtmalarini topadi.
     */
    List<SaleOrder> findByClient_IdAndDebtSumGreaterThan(Long clientId, BigDecimal amount);

    /**
     * Qarzdor mijozlar ro'yxatini (admin panel uchun) ixtiyoriy filtrlar bilan qaytaradi:
     * - startDate/endDate: orderDate bo'yicha sana oralig'i (ikkalasi ham null bo'lsa cheklanmaydi)
     * - userId: buyurtmaga biriktirilgan xodim bo'yicha (null bo'lsa cheklanmaydi)
     */
    @Query("SELECT so FROM SaleOrder so WHERE so.debtSum > 0 " +
            "AND (:startDate IS NULL OR so.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR so.orderDate <= :endDate) " +
            "AND (:userId IS NULL OR so.user.id = :userId)")
    List<SaleOrder> findDebtOrders(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId
    );

    @Query("SELECT so FROM SaleOrder so WHERE so.client.id = :clientId AND so.warehouse.id = :warehouseId")
    Page<SaleOrder> findByClientIdAndWarehouseId(
            @Param("clientId") Long clientId,
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );

    Optional<SaleOrder> findByIdAndWarehouseId(Long id, Long warehouseId);

//    Optional<SaleOrder> findByIdAndStatusNotDeleted(Long id);
}