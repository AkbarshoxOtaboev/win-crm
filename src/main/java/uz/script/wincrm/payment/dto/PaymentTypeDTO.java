package uz.script.wincrm.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PaymentType DTO", description = "Payment type data transfer object for creating and updating")
public class PaymentTypeDTO {

    @NotBlank(message = "Payment type name is required")
    @Schema(description = "Payment type name", example = "Naqd pul", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}