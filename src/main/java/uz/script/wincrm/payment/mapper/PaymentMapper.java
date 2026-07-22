package uz.script.wincrm.payment.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.payment.Payment;
import uz.script.wincrm.payment.dto.PaymentDTO;
import uz.script.wincrm.payment.response.PaymentResponse;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }
        return Payment.builder()
                .paymentAmount(dto.getPaymentAmount())
                .paymentDate(dto.getPaymentDate())
                .comment(dto.getComment())
                .build();
    }

    public void updateEntity(Payment entity, PaymentDTO dto) {
        if (dto.getPaymentAmount() != null) {
            entity.setPaymentAmount(dto.getPaymentAmount());
        }
        if (dto.getPaymentDate() != null) {
            entity.setPaymentDate(dto.getPaymentDate());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
    }

    public PaymentResponse toResponse(Payment entity) {
        if (entity == null) {
            return null;
        }
        return PaymentResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientFullName(entity.getClient() != null ? entity.getClient().getFullName() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userFullName(entity.getUser() != null ? entity.getUser().getFullName() : null)
                .saleOrderId(entity.getSaleOrder() != null ? entity.getSaleOrder().getId() : null)
                .paymentTypeId(entity.getPaymentType() != null ? entity.getPaymentType().getId() : null)
                .paymentTypeName(entity.getPaymentType() != null ? entity.getPaymentType().getName() : null)
                .paymentAmount(entity.getPaymentAmount())
                .paymentDate(entity.getPaymentDate())
                .comment(entity.getComment())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdUsername(entity.getCreatedUsername())
                .build();
    }
}