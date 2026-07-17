package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.sale.enums.SalesOrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "SaleOrder History Response", description = "Single status change event in a sale order's chronology")
public class SaleOrderHistoryResponse {

    @Schema(description = "Unique history record identifier", example = "1")
    private Long id;

    @Schema(description = "Sale order ID this history entry belongs to", example = "1042")
    private Long saleOrderId;

    @Schema(description = "Previous status (null for the very first record - order creation)", example = "NEW")
    private SalesOrderStatus fromStatus;

    @Schema(description = "New status that was set", example = "CONFIRMED")
    private SalesOrderStatus toStatus;

    @Schema(description = "ID of the user who changed the status", example = "3")
    private Long changedByUserId;

    @Schema(description = "Full name of the user who changed the status", example = "Ali Valiyev")
    private String changedByUserFullName;

    @Schema(description = "Optional comment about the status change")
    private String comment;

    @Schema(description = "Date and time when this status was set", example = "2026-07-13T16:42:00")
    private LocalDateTime changedAt;
}