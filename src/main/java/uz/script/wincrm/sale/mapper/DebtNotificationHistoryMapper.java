package uz.script.wincrm.sale.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.sale.DebtNotificationHistory;
import uz.script.wincrm.sale.response.DebtNotificationHistoryResponse;

@Component
public class DebtNotificationHistoryMapper {

    public DebtNotificationHistoryResponse toResponse(DebtNotificationHistory entity) {
        if (entity == null) {
            return null;
        }
        return DebtNotificationHistoryResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientFullName(entity.getClient() != null ? entity.getClient().getFullName() : null)
                .phone(entity.getPhone())
                .message(entity.getMessage())
                .totalDebtAmount(entity.getTotalDebtAmount())
                .sentAt(entity.getSentAt())
                .status(entity.getDebtNotificationStatus())
                .errorMessage(entity.getErrorMessage())
                .build();
    }
}