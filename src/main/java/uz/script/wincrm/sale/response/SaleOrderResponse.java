package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Sale Order Response", description = "Sale Order information returned by the API")
public class SaleOrderResponse {

    @Schema(description = "Unique sale order identifier", example = "1")
    private Long id;

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "ID of the user (salesperson) who placed the order", example = "1")
    private Long userId;

    @Schema(description = "Full name of the user (salesperson) who placed the order", example = "Ali Valiyev")
    private String userFullName;

    @Schema(description = "Comment/Notes about the sale order")
    private String comment;

    @Schema(description = "Order date and time", example = "2026-07-04T09:30:15")
    private LocalDateTime orderDate;

    @Schema(description = "Buyurtma tayyor bo'lishi rejalashtirilgan sana", example = "2026-07-17T00:00:00")
    private LocalDateTime plannedReadyDate;

    @Schema(description = "Mijozga yetkazilishi rejalashtirilgan sana", example = "2026-07-18T00:00:00")
    private LocalDateTime plannedDeliveryDate;

    @Schema(description = "Total sum of the sale order", example = "5000.00")
    private BigDecimal totalSum;

    @Schema(description = "Paid sum of the sale order", example = "3000.00")
    private BigDecimal paidSum;

    @Schema(description = "Remaining debt sum of the sale order", example = "2000.00")
    private BigDecimal debtSum;

    @Schema(description = "Current sale order status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Sale order processing status", example = "CONFIRMED")
    private SalesOrderStatus orderStatus;

    @Schema(description = "Date and time when the sale order was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the sale order was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user who created the sale order record", example = "1")
    private Long createdBy;
}