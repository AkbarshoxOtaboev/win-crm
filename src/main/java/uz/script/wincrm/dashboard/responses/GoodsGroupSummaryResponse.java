package uz.script.wincrm.dashboard.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Goods Group Summary Response", description = "GoodsGroup bo'yicha jamlangan savdo statistikasi")
public class GoodsGroupSummaryResponse {

    @Schema(description = "Goods group ID", example = "1")
    private Long goodsGroupId;

    @Schema(description = "Goods group nomi", example = "Beverages")
    private String goodsGroupName;

    @Schema(description = "Shu guruhdan sotilgan umumiy miqdor", example = "3400")
    private BigDecimal totalCount;

    @Schema(description = "Shu guruhdan sotilgan umumiy summa", example = "54200000.00")
    private BigDecimal totalAmount;

    /**
     * JPQL "SELECT new ...(...)" constructor expression uchun.
     * Parametr tartibini o'zgartirmang — repository so'rovlari shu tartibga mos.
     */
    public GoodsGroupSummaryResponse(Long goodsGroupId, String goodsGroupName, BigDecimal totalCount, BigDecimal totalAmount) {
        this.goodsGroupId = goodsGroupId;
        this.goodsGroupName = goodsGroupName;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }
}