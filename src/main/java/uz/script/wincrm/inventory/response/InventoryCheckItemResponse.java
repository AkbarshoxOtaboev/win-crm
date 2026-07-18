package uz.script.wincrm.inventory.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Schema(name = "Inventory Check Item Response", description = "Inventarizatsiya qatoridagi bitta mahsulot ma'lumoti")
public class InventoryCheckItemResponse {

    @Schema(description = "Qator identifikatori", example = "10")
    private Long id;

    @Schema(description = "Mahsulot identifikatori", example = "5")
    private Long goodsId;

    @Schema(description = "Mahsulot nomi", example = "Coca-Cola 1.5L")
    private String goodsName;

    @Schema(description = "Inventarizatsiya boshlanganda tizimdagi qoldiq", example = "50")
    private BigDecimal systemCount;

    @Schema(description = "Jismonan sanalgan haqiqiy qoldiq", example = "48")
    private BigDecimal actualCount;

    @Schema(description = "Farq (actualCount - systemCount). Manfiy - kamomad, musbat - ortiqcha", example = "-2")
    private BigDecimal difference;

    @Schema(description = "Izoh", example = "2 dona shikastlangan holda topildi")
    private String comment;
}