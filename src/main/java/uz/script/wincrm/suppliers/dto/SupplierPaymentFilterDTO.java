package uz.script.wincrm.suppliers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Supplier payment filter request")
public class SupplierPaymentFilterDTO {

    @Schema(description = "Supplier id", example = "1")
    private Long supplierId;

    @Schema(description = "Payment type id", example = "1")
    private Long paymentTypeId;

    @Schema(description = "Minimum paid sum", example = "0")
    private BigDecimal minSumm;

    @Schema(description = "Maximum paid sum", example = "1000000")
    private BigDecimal maxSumm;

    @Schema(description = "Payment status", example = "ACTIVE")
    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Paid from date", example = "2026-01-01T00:00:00")
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Paid to date", example = "2026-12-31T23:59:59")
    private LocalDateTime toDate;
}