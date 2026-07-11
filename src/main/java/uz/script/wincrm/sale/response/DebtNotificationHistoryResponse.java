package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.sale.enums.DebtNotificationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Debt Notification History Response", description = "Qarz haqida yuborilgan SMS tarixi")
public class DebtNotificationHistoryResponse {

    @Schema(description = "Unique history record ID", example = "1")
    private Long id;

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(description = "SMS yuborilgan telefon raqami", example = "998901234567")
    private String phone;

    @Schema(description = "Yuborilgan SMS matni")
    private String message;

    @Schema(description = "SMS yuborilgan paytdagi umumiy qarz summasi", example = "1500000.00")
    private BigDecimal totalDebtAmount;

    @Schema(description = "SMS yuborilgan sana va vaqt")
    private LocalDateTime sentAt;

    @Schema(description = "SMS holati", example = "SUCCESS")
    private DebtNotificationStatus status;

    @Schema(description = "Agar yuborilmagan bo'lsa, xatolik matni")
    private String errorMessage;
}