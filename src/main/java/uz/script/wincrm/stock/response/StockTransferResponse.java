package uz.script.wincrm.stock.response;

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
@Schema(name = "Stock Transfer Response", description = "Stock transfer haqidagi ma'lumot")
public class StockTransferResponse {

    @Schema(description = "Transfer identifikatori", example = "1")
    private Long id;

    @Schema(description = "Goods identifikatori", example = "1")
    private Long goodsId;

    @Schema(description = "Goods nomi", example = "Coca-Cola 1.5L")
    private String goodsName;

    @Schema(description = "Manba ombor identifikatori", example = "1")
    private Long fromWarehouseId;

    @Schema(description = "Manba ombor nomi", example = "Markaziy ombor")
    private String fromWarehouseName;

    @Schema(description = "Maqsad ombor identifikatori", example = "2")
    private Long toWarehouseId;

    @Schema(description = "Maqsad ombor nomi", example = "Filial ombori")
    private String toWarehouseName;

    @Schema(description = "Ko'chirilgan miqdor", example = "50")
    private BigDecimal count;

    @Schema(description = "Izoh", example = "Filial uchun ko'chirish")
    private String comment;

    @Schema(description = "Holati", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Yaratilgan vaqti", example = "2026-07-18T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Yangilangan vaqti", example = "2026-07-18T09:30:15")
    private LocalDateTime updatedAt;

    @Schema(description = "Transferni amalga oshirgan foydalanuvchi", example = "admin")
    private String createdUsername;
}
