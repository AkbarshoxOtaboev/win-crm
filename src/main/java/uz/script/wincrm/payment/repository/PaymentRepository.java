package uz.script.wincrm.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.payment.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findBySaleOrderId(Long saleOrderId, Pageable pageable);

    List<Payment> findBySaleOrderId(Long saleOrderId);

    Page<Payment> findByPaymentTypeId(Long paymentTypeId, Pageable pageable);

    Page<Payment> findByClientId(Long clientId, Pageable pageable);

    List<Payment> findByClientId(Long clientId);

    /**
     * Berilgan sana-vaqt oralig'idagi barcha to'lovlarni, PaymentType bilan birga
     * (N+1 muammosining oldini olish uchun JOIN FETCH) qaytaradi.
     * Dashboard hisobotlarida (PaymentType bo'yicha jamlash, kunlik taqsimot) ishlatiladi.
     */
    @Query("SELECT p FROM Payment p JOIN FETCH p.paymentType " +
            "WHERE p.paymentDate BETWEEN :fromDate AND :toDate " +
            "ORDER BY p.paymentDate ASC")
    List<Payment> findAllInRangeWithType(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM Payment p WHERE p.client.id = :clientId")
    BigDecimal sumPaymentAmountByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM Payment p " +
            "WHERE p.client.id = :clientId AND p.paymentDate BETWEEN :fromDateTime AND :toDateTime")
    BigDecimal sumPaymentAmountByClientIdAndDateRange(
            @Param("clientId") Long clientId,
            @Param("fromDateTime") LocalDateTime fromDateTime,
            @Param("toDateTime") LocalDateTime toDateTime
    );

    /**
     * Berilgan user (to'lovni qabul qilgan/kiritgan xodim) bo'yicha barcha
     * paymentAmount qiymatlarining yig'indisini qaytaradi.
     */
    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM Payment p WHERE p.user.id = :userId")
    BigDecimal sumPaymentAmountByUserId(@Param("userId") Long userId);

    // ---------------------------------------------------------------
    // Telegram bot uchun — lazy proxy'siz, yengil proyeksiyalar
    // LEFT JOIN ishlatilgan — paymentType yoki saleOrder null bo'lsa ham
    // yozuv natijadan tushib qolmasligi uchun (ichki JOIN bo'lganda tushib qolardi).
    // ---------------------------------------------------------------

    @Query("SELECT new uz.script.wincrm.telegram.view.PaymentView(" +
            "p.id, p.paymentDate, p.paymentAmount, pt.name, so.id, p.comment) " +
            "FROM Payment p " +
            "LEFT JOIN p.paymentType pt " +
            "LEFT JOIN p.saleOrder so " +
            "WHERE p.client.id = :clientId " +
            "ORDER BY p.paymentDate DESC")
    List<uz.script.wincrm.telegram.view.PaymentView> findPaymentViewsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT new uz.script.wincrm.telegram.view.PaymentView(" +
            "p.id, p.paymentDate, p.paymentAmount, pt.name, p.saleOrder.id, p.comment) " +
            "FROM Payment p LEFT JOIN p.paymentType pt " +
            "WHERE p.saleOrder.id = :saleOrderId ORDER BY p.paymentDate ASC")
    List<uz.script.wincrm.telegram.view.PaymentView> findPaymentViewsBySaleOrderId(@Param("saleOrderId") Long saleOrderId);
}