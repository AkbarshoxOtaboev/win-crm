package uz.script.wincrm.payment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "PaymentType Response", description = "Payment type information returned by the API")
public class PaymentTypeResponse {

    @Schema(description = "Unique payment type identifier", example = "1")
    private Long id;

    @Schema(description = "Payment type name", example = "Naqd pul")
    private String name;

    @Schema(description = "Current status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the payment type was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the payment type was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the payment type", example = "admin")
    private String createdUsername;
}