package uz.script.wincrm.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SaleOrderItem DTO", description = "Sale Order Item data transfer object for creating and updating")
public class SaleOrderItemDTO {

    @NotNull(message = "Warehouse ID is required")
    @Schema(description = "Warehouse ID", example = "1", required = true)
    private Long warehouseId;

    @NotNull(message = "Sale Order ID is required")
    @Schema(description = "Sale Order ID", example = "1", required = true)
    private Long saleOrderId;

    @NotNull(message = "Client ID is required")
    @Schema(description = "Client ID", example = "1", required = true)
    private Long clientId;

    @NotNull(message = "Goods ID is required")
    @Schema(description = "Goods/Product ID", example = "1", required = true)
    private Long goodsId;

    @NotNull(message = "Price cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price cost must be greater than 0")
    @Schema(description = "Cost price of the product", example = "1000.00", required = true)
    private BigDecimal priceCost;

    @NotNull(message = "Price selling is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price selling must be greater than 0")
    @Schema(description = "Selling price of the product", example = "1500.00", required = true)
    private BigDecimal priceSelling;

    @Schema(description = "Product width")
    private BigDecimal width;

    @Schema(description = "Product height")
    private BigDecimal height;

    @NotNull(message = "Count is required")
    @DecimalMin(value = "0.0", message = "Count must be greater than 0")
    @Schema(description = """
            Product count to be sold
            ⭐ IMPORTANT: This count will be validated against warehouse stock!
            If warehouse has 10 items and you request 12, the request will be rejected!
            """, example = "5", required = true)
    private BigDecimal count;

    @NotNull(message = "Arrival date is required")
    @Schema(description = "Product arrival date and time", required = true)
    private LocalDateTime arrivalDate;
}