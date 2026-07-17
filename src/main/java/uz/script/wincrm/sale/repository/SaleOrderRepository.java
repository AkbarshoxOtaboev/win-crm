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
     *
     * DIQQAT: "CAST(:param AS ...)" bu yerda shart — PostgreSQL JDBC drayveri
     * "(:param IS NULL OR ...)" shablonida parametr faqat IS NULL tekshiruvida
     * ishlatilganda uning tipini avtomatik aniqlay olmaydi va
     * "не удалось определить тип данных параметра $1" xatosini beradi.
     * Aniq CAST bu muammoni butunlay bartaraf etadi.
     */
    @Query("SELECT so FROM SaleOrder so WHERE so.debtSum > 0 " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR so.orderDate >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR so.orderDate <= :endDate) " +
            "AND (CAST(:userId AS long) IS NULL OR so.user.id = :userId)")
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

    /**
     * Berilgan davrda eng ko'p savdo qilgan TOP sotuvchilarni (User) qaytaradi.
     * Har bir qator: [0] = User entity, [1] = buyurtmalar soni (Long), [2] = umumiy summa (BigDecimal).
     * DELETED buyurtmalar SaleOrder'dagi @SQLRestriction("status <> 'DELETED'") orqali avtomatik chiqarib tashlanadi.
     */
    @Query("""
            SELECT so.user, COUNT(so), COALESCE(SUM(so.totalSum), 0)
            FROM SaleOrder so
            WHERE so.user IS NOT NULL
              AND so.orderDate BETWEEN :startDate AND :endDate
            GROUP BY so.user
            ORDER BY SUM(so.totalSum) DESC
            """)
    List<Object[]> findTopSellersByAmount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(s.totalSum), 0) FROM SaleOrder s WHERE s.client.id = :clientId")
    BigDecimal sumTotalSumByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COALESCE(SUM(s.totalSum), 0) FROM SaleOrder s " +
            "WHERE s.client.id = :clientId AND s.orderDate BETWEEN :fromDateTime AND :toDateTime")
    BigDecimal sumTotalSumByClientIdAndDateRange(
            @Param("clientId") Long clientId,
            @Param("fromDateTime") LocalDateTime fromDateTime,
            @Param("toDateTime") LocalDateTime toDateTime
    );

    /**
     * Berilgan user (sotuvchi) uchun buyurtmalar sonini va umumiy totalSum yig'indisini
     * bitta so'rovda qaytaradi. Natija har doim bitta qatordan iborat bo'ladi:
     * row[0] = buyurtmalar soni (Long), row[1] = umumiy summa (BigDecimal).
     */
    @Query("SELECT COUNT(so), COALESCE(SUM(so.totalSum), 0) FROM SaleOrder so WHERE so.user.id = :userId")
    List<Object[]> countAndSumByUserId(@Param("userId") Long userId);
}