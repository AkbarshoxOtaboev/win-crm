package uz.script.wincrm.warehouse.response;

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
@Schema(name = "Warehouse Order Response", description = "Warehouse order information returned by the API")
public class WarehouseOrderResponse {

    @Schema(description = "Unique warehouse order identifier", example = "1")
    private Long id;

    @Schema(description = "Supplier identifier", example = "1")
    private Long supplierId;

    @Schema(description = "Supplier name", example = "Coca-Cola Distribution LLC")
    private String supplierName;

    @Schema(description = "Warehouse identifier", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "Comment about the order", example = "Urgent delivery, handle with care")
    private String comment;

    @Schema(description = "Arrival date and time", example = "2026-07-10T14:00:00")
    private LocalDateTime arrivalDate;

    @Schema(description = "Total sum of the order, computed from its items", example = "1250000.00")
    private BigDecimal totalSum;

    @Schema(description = "Current order status", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Date and time when the order was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the order was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the order", example = "admin")
    private String createdUsername;
}
