package uz.script.wincrm.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.payment.Payment;

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
}