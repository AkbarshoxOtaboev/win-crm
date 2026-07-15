package uz.script.wincrm.dashboard.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Schema(name = "Payment Type Summary Response", description = "Berilgan davrda PaymentType bo'yicha jamlangan to'lov statistikasi")
public class PaymentTypeSummaryResponse {

    @Schema(description = "Payment type ID", example = "1")
    private Long paymentTypeId;

    @Schema(description = "Payment type nomi", example = "Naqd pul")
    private String paymentTypeName;

    @Schema(description = "Shu davrda shu turdagi to'lovlar umumiy summasi", example = "24500000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Shu davrda shu turdagi to'lovlar soni", example = "37")
    private long paymentCount;
}