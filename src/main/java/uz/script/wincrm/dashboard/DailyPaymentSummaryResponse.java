package uz.script.wincrm.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(name = "Daily Payment Summary Response", description = "Bir kunlik to'lovlar statistikasi, PaymentType bo'yicha taqsimlangan")
public class DailyPaymentSummaryResponse {

    @Schema(description = "Sana", example = "2026-07-09")
    private LocalDate date;

    @Schema(description = "Shu kundagi jami to'lov summasi", example = "3200000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Shu kundagi to'lovlarning PaymentType bo'yicha taqsimoti")
    private List<PaymentTypeAmount> byType;

    @Getter
    @Setter
    @Builder
    @Schema(name = "Payment Type Amount", description = "Bitta PaymentType bo'yicha summa")
    public static class PaymentTypeAmount {

        @Schema(description = "Payment type ID", example = "1")
        private Long paymentTypeId;

        @Schema(description = "Payment type nomi", example = "Naqd pul")
        private String paymentTypeName;

        @Schema(description = "Shu kunda shu turdagi to'lovlar summasi", example = "1500000.00")
        private BigDecimal amount;
    }
}