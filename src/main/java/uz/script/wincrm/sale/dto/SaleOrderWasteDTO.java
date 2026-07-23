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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "SaleOrderWaste DTO",
        description = "Buyurtma bo'yicha ishlab chiqarishdan ortib qolgan materialni qayd etish uchun DTO. " +
                "Faqat INFO sifatida saqlanadi - stock yoki buyurtma summalariga ta'sir qilmaydi."
)
public class SaleOrderWasteDTO {

    @NotNull(message = "Sale Order ID is required")
    @Schema(description = "Qaysi buyurtmaga tegishli ekanligi", example = "1", required = true)
    private Long saleOrderId;

    @NotNull(message = "Goods ID is required")
    @Schema(description = "Ortib qolgan material qaysi Goods'ga tegishli ekanligi", example = "1", required = true)
    private Long goodsId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    @Schema(description = "Ortib qolgan material miqdori (Goods'ning o'lchov birligida)", example = "3.5", required = true)
    private BigDecimal quantity;

    @Schema(description = "Ortib qolgan bo'lak kengligi (ixtiyoriy)")
    private BigDecimal width;

    @Schema(description = "Ortib qolgan bo'lak balandligi/uzunligi (ixtiyoriy)")
    private BigDecimal height;

    @Schema(description = "Izoh (ixtiyoriy)")
    private String comment;
}