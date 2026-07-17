package uz.script.wincrm.clients.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Client Balance Response", description = "Client balance information returned by the API")
public class ClientBalanceResponse {

    @Schema(description = "Unique balance identifier", example = "1")
    private Long id;

    @Schema(description = "Client identifier", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(
            description = "Total purchase amount. If periodFrom/periodTo are present, scoped to that period; " +
                    "otherwise represents the all-time cumulative value",
            example = "15000000"
    )
    private BigDecimal totalPurchase;

    @Schema(
            description = "Total paid amount. If periodFrom/periodTo are present, scoped to that period; " +
                    "otherwise represents the all-time cumulative value",
            example = "10000000"
    )
    private BigDecimal totalPaid;

    @Schema(description = "Debt amount (totalPurchase - totalPaid) for the same scope as above", example = "5000000")
    private BigDecimal totalDebt;

    @Schema(description = "Date and time when the all-time balance was last recalculated", example = "2026-07-17T10:15:30")
    private LocalDateTime lastUpdated;

    @Schema(description = "Start date of the reporting period (null when response is all-time)", example = "2026-07-01")
    private LocalDate periodFrom;

    @Schema(description = "End date of the reporting period (null when response is all-time)", example = "2026-07-31")
    private LocalDate periodTo;
}