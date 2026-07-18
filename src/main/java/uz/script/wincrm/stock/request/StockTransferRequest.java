package uz.script.wincrm.stock.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Stock Transfer Request", description = "Ikki ombor orasida mahsulot ko'chirish uchun so'rov")
public class StockTransferRequest {

    @NotNull(message = "goodsId bo'sh bo'lishi mumkin emas")
    @Schema(description = "Goods identifier", example = "1")
    private Long goodsId;

    @NotNull(message = "fromWarehouseId bo'sh bo'lishi mumkin emas")
    @Schema(description = "Manba ombor identifikatori", example = "1")
    private Long fromWarehouseId;

    @NotNull(message = "toWarehouseId bo'sh bo'lishi mumkin emas")
    @Schema(description = "Maqsad ombor identifikatori", example = "2")
    private Long toWarehouseId;

    @NotNull(message = "count bo'sh bo'lishi mumkin emas")
    @DecimalMin(value = "0.01", message = "count 0 dan katta bo'lishi kerak")
    @Schema(description = "Ko'chiriladigan miqdor", example = "50")
    private BigDecimal count;

    @Schema(description = "Ixtiyoriy izoh", example = "Filial uchun ko'chirish")
    private String comment;
}
