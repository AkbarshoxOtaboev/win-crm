package uz.script.wincrm.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.payment.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findBySaleOrderId(Long saleOrderId, Pageable pageable);

    List<Payment> findBySaleOrderId(Long saleOrderId);

    Page<Payment> findByPaymentTypeId(Long paymentTypeId, Pageable pageable);

    Page<Payment> findByClientId(Long clientId, Pageable pageable);

    List<Payment> findByClientId(Long clientId);
}