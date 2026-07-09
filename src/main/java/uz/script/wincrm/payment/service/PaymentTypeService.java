package uz.script.wincrm.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.payment.dto.PaymentTypeDTO;
import uz.script.wincrm.payment.response.PaymentTypeResponse;

public interface PaymentTypeService {

    PaymentTypeResponse create(PaymentTypeDTO dto);

    PaymentTypeResponse findById(Long id);

    Page<PaymentTypeResponse> fetchAll(Pageable pageable);

    PaymentTypeResponse update(Long id, PaymentTypeDTO dto);

    void delete(Long id);
}