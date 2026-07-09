package uz.script.wincrm.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.payment.dto.PaymentDTO;
import uz.script.wincrm.payment.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse create(PaymentDTO dto);

    PaymentResponse findById(Long id);

    Page<PaymentResponse> fetchAll(Pageable pageable);

    Page<PaymentResponse> fetchByClientId(Long clientId, Pageable pageable);

    Page<PaymentResponse> fetchBySaleOrderId(Long saleOrderId, Pageable pageable);

    Page<PaymentResponse> fetchByPaymentTypeId(Long paymentTypeId, Pageable pageable);

    PaymentResponse update(Long id, PaymentDTO dto);

    void delete(Long id);
}