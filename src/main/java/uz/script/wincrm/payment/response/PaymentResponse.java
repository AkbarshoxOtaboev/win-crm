package uz.script.wincrm.payment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Payment Response", description = "Payment information returned by the API")
public class PaymentResponse {

    @Schema(description = "Unique payment identifier", example = "1")
    private Long id;

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(description = "Sale order ID", example = "1")
    private Long saleOrderId;

    @Schema(description = "Payment type ID", example = "1")
    private Long paymentTypeId;

    @Schema(description = "Payment type name", example = "Naqd pul")
    private String paymentTypeName;

    @Schema(description = "Payment amount", example = "1000.00")
    private BigDecimal paymentAmount;

    @Schema(description = "Payment date and time", example = "2026-07-04T09:30:15")
    private LocalDateTime paymentDate;

    @Schema(description = "Comment/Notes about the payment")
    private String comment;

    @Schema(description = "Current payment status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the payment was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the payment was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the payment", example = "admin")
    private String createdUsername;
}