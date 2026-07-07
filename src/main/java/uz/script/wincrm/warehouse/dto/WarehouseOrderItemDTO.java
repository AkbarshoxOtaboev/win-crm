package uz.script.wincrm.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "Warehouse Order Item DTO", description = "Warehouse order item creation and update request")
public class WarehouseOrderItemDTO {

    @NotNull(message = "Warehouse id is required")
    @Schema(description = "Warehouse identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long warehouseId;

    @NotNull(message = "Warehouse order id is required")
    @Schema(description = "Warehouse order identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long warehouseOrderId;

    @NotNull(message = "Supplier id is required")
    @Schema(description = "Supplier identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long supplierId;

    @NotNull(message = "Goods id is required")
    @Schema(description = "Goods identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long goodsId;

    @NotNull(message = "Price cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cost must not be negative")
    @Schema(description = "Cost price for this batch", example = "8500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal priceCost;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Selling price must not be negative")
    @Schema(description = "Selling price for this batch", example = "12000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal priceSelling;

    @DecimalMin(value = "0.0", inclusive = true, message = "Weight must not be negative")
    @Schema(description = "Weight of the batch", example = "150.5")
    private BigDecimal weight;

    @DecimalMin(value = "0.0", inclusive = true, message = "Height must not be negative")
    @Schema(description = "Height of the batch", example = "12.0")
    private BigDecimal height;

    @NotNull(message = "Count is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Count must be greater than 0")
    @Schema(description = "Quantity received", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal count;

    @NotNull(message = "Arrival date is required")
    @Schema(description = "Arrival date and time", example = "2026-07-10T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime arrivalDate;
}
