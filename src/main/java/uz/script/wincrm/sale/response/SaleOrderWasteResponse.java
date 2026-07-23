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
@Schema(name = "Sale Order Waste Response", description = "Buyurtma bo'yicha ortib qolgan material haqidagi ma'lumot")
public class SaleOrderWasteResponse {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Sale Order ID", example = "1")
    private Long saleOrderId;

    @Schema(description = "Goods ID", example = "1")
    private Long goodsId;

    @Schema(description = "Yozuv yaratilgan paytdagi material nomi", example = "Shisha 4mm")
    private String goodsName;

    @Schema(description = "Yozuv yaratilgan paytdagi o'lchov birligi", example = "m2")
    private String unitName;

    @Schema(description = "Ortib qolgan material miqdori", example = "3.5")
    private BigDecimal quantity;

    @Schema(description = "Ortib qolgan bo'lak kengligi")
    private BigDecimal width;

    @Schema(description = "Ortib qolgan bo'lak balandligi/uzunligi")
    private BigDecimal height;

    @Schema(description = "Izoh")
    private String comment;

    @Schema(description = "Current status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Yaratilgan sana", example = "2026-07-23T10:15:00")
    private LocalDateTime createdAt;

    @Schema(description = "Oxirgi yangilangan sana", example = "2026-07-23T10:15:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Yozuvni yaratgan foydalanuvchi ID", example = "1")
    private Long createdBy;
}