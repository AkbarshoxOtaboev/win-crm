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
@Schema(name = "Warehouse Order Item Response", description = "Warehouse order item information returned by the API")
public class WarehouseOrderItemResponse {

    @Schema(description = "Unique warehouse order item identifier", example = "1")
    private Long id;

    @Schema(description = "Warehouse identifier", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "Warehouse order identifier", example = "1")
    private Long warehouseOrderId;

    @Schema(description = "Supplier identifier", example = "1")
    private Long supplierId;

    @Schema(description = "Supplier name", example = "Coca-Cola Distribution LLC")
    private String supplierName;

    @Schema(description = "Goods identifier", example = "1")
    private Long goodsId;

    @Schema(description = "Goods name", example = "Coca-Cola 1.5L")
    private String goodsName;

    @Schema(description = "Cost price for this batch", example = "8500.00")
    private BigDecimal priceCost;

    @Schema(description = "Selling price for this batch", example = "12000.00")
    private BigDecimal priceSelling;

    @Schema(description = "Weight of the batch", example = "150.5")
    private BigDecimal weight;

    @Schema(description = "Height of the batch", example = "12.0")
    private BigDecimal height;

    @Schema(description = "Quantity received", example = "100")
    private BigDecimal count;

    @Schema(description = "Arrival date and time", example = "2026-07-10T14:00:00")
    private LocalDateTime arrivalDate;

    @Schema(description = "Current item status", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Date and time when the item was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the item was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the item", example = "admin")
    private String createdUsername;
}
