package uz.script.wincrm.stock.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.stock.enums.StockStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Stock History Response", description = "Stock movement history information returned by the API")
public class StockHistoryResponse {

    @Schema(description = "Unique stock history identifier", example = "1")
    private Long id;

    @Schema(description = "Goods identifier", example = "1")
    private Long goodsId;

    @Schema(description = "Goods name", example = "Coca-Cola 1.5L")
    private String goodsName;

    @Schema(description = "Warehouse identifier", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "Movement type", example = "IN", implementation = StockStatus.class)
    private StockStatus stockStatus;

    @Schema(description = "Quantity moved in this operation", example = "100")
    private BigDecimal count;

    @Schema(description = "Stock balance right after this operation", example = "350")
    private BigDecimal balanceAfter;

    @Schema(description = "Optional comment or reason for the movement", example = "Warehouse order item #12 kirim qilindi")
    private String comment;

    @Schema(description = "Date and time when the movement was recorded", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the record was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who triggered the movement", example = "admin")
    private String createdUsername;
}