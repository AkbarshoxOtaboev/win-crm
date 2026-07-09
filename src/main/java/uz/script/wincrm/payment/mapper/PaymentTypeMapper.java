package uz.script.wincrm.payment.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.payment.dto.PaymentTypeDTO;
import uz.script.wincrm.payment.response.PaymentTypeResponse;

@Component
public class PaymentTypeMapper {

    public PaymentType toEntity(PaymentTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        return PaymentType.builder()
                .name(dto.getName())
                .build();
    }

    public void updateEntity(PaymentType entity, PaymentTypeDTO dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
    }

    public PaymentTypeResponse toResponse(PaymentType entity) {
        if (entity == null) {
            return null;
        }
        return PaymentTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdUsername(entity.getCreatedUsername())
                .build();
    }
}