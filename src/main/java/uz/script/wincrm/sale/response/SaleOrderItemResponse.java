package uz.script.wincrm.sale.response;

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
@Schema(name = "Sale Order Item Response", description = "Sale Order Item information returned by the API")
public class SaleOrderItemResponse {

    @Schema(description = "Unique sale order item identifier", example = "1")
    private Long id;

    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "Sale Order ID", example = "1")
    private Long saleOrderId;

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(description = "Goods/Product ID", example = "1")
    private Long goodsId;

    @Schema(description = "Product name", example = "Cement")
    private String goodsName;

    @Schema(description = "Cost price of the product", example = "1000.00")
    private BigDecimal priceCost;

    @Schema(description = "Selling price of the product", example = "1500.00")
    private BigDecimal priceSelling;

    @Schema(description = "Product width")
    private BigDecimal width;

    @Schema(description = "Product height")
    private BigDecimal height;

    @Schema(description = "Product count", example = "5")
    private BigDecimal count;

    @Schema(description = "Product arrival date and time")
    private LocalDateTime arrivalDate;

    @Schema(description = "Current sale order item status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the item was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the item was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the item", example = "admin")
    private String createdUsername;
}