package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Material (Goods) kesimida jamlangan ortib qolgan miqdor hisoboti.
 * Repository'da JPQL "SELECT new ...SaleOrderWasteSummaryResponse(...)" constructor-expression
 * orqali to'g'ridan-to'g'ri to'ldiriladi (Dashboard modulidagi TopGoodsResponse pattern'iga o'xshab).
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(name = "Sale Order Waste Summary Response", description = "Material bo'yicha jamlangan ortib qolgan miqdor")
public class SaleOrderWasteSummaryResponse {

    @Schema(description = "Goods ID", example = "1")
    private Long goodsId;

    @Schema(description = "Yig'indi hisoblangan paytdagi material nomi", example = "Shisha 4mm")
    private String goodsName;

    @Schema(description = "Jami ortib qolgan miqdor", example = "27.8")
    private BigDecimal totalQuantity;
}