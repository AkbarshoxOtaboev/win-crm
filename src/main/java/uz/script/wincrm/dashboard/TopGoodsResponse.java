package uz.script.wincrm.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Top Goods Response", description = "Berilgan sana oralig'ida eng ko'p sotilgan mahsulot statistikasi")
public class TopGoodsResponse {

    @Schema(description = "Goods ID", example = "1")
    private Long goodsId;

    @Schema(description = "Goods nomi", example = "Coca-Cola 1.5L")
    private String goodsName;

    @Schema(description = "Shu davrda sotilgan umumiy miqdor", example = "1250")
    private BigDecimal totalCount;

    @Schema(description = "Shu davrda sotilgan umumiy summa", example = "18750000.00")
    private BigDecimal totalAmount;

    /**
     * JPQL "SELECT new ...(...)" constructor expression uchun.
     * Parametr tartibini o'zgartirmang — repository so'rovlari shu tartibga mos.
     */
    public TopGoodsResponse(Long goodsId, String goodsName, BigDecimal totalCount, BigDecimal totalAmount) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }
}