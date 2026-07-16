package uz.script.wincrm.suppliers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Supplier balance filter request")
public class SupplierBalanceFilterDTO {

    @Schema(description = "Supplier name", example = "Artel")
    private String supplierName;

    @Schema(description = "Minimum debt", example = "0")
    private BigDecimal minDebt;

    @Schema(description = "Maximum debt", example = "1000000")
    private BigDecimal maxDebt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Last updated from", example = "2026-01-01T00:00:00")
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Last updated to", example = "2026-12-31T23:59:59")
    private LocalDateTime toDate;
}