package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.sale.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Sale Order Discount History Response",
        description = "Buyurtmaga qo'llangan chegirma tarixi yozuvi")
public class SaleOrderDiscountHistoryResponse {

    @Schema(description = "History yozuvi ID", example = "1")
    private Long id;

    @Schema(description = "Buyurtma ID", example = "1")
    private Long saleOrderId;

    @Schema(description = "Chegirma turi", example = "PERCENTAGE")
    private DiscountType discountType;

    @Schema(description = "Kiritilgan chegirma qiymati", example = "10")
    private BigDecimal discountValue;

    @Schema(description = "Qo'llangan aniq chegirma summasi", example = "50000.00")
    private BigDecimal discountAmount;

    @Schema(description = "Oldingi chegirma summasi", example = "0.00")
    private BigDecimal previousDiscountAmount;

    @Schema(description = "Chegirmagacha bo'lgan asl summa", example = "500000.00")
    private BigDecimal originalTotalSum;

    @Schema(description = "Chegirmadan keyingi yakuniy summa", example = "450000.00")
    private BigDecimal totalSumAfter;

    @Schema(description = "Yozuv yaratilgan sana", example = "2026-07-20T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Chegirmani qo'llagan foydalanuvchi ID", example = "1")
    private Long createdBy;
}