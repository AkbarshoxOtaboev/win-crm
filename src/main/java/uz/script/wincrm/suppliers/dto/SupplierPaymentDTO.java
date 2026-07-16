package uz.script.wincrm.suppliers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Supplier payment create/update request")
public class SupplierPaymentDTO {

    @NotNull(message = "Supplier is required")
    @Schema(description = "Supplier id", example = "1")
    private Long supplierId;

    @NotNull(message = "Paid sum is required")
    @DecimalMin(value = "0.01", message = "Paid sum must be greater than 0")
    @Schema(description = "Paid amount", example = "500000")
    private BigDecimal paidSumm;

    @NotNull(message = "Paid date is required")
    @Schema(description = "Payment date", example = "2026-07-16T10:00:00")
    private LocalDateTime paidDate;

    @Schema(description = "Comment")
    private String comment;

    @NotNull(message = "Payment type is required")
    @Schema(description = "Payment type id", example = "1")
    private Long paymentTypeId;
}