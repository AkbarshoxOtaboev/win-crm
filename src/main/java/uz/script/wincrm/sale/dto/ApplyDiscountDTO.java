package uz.script.wincrm.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.script.wincrm.sale.enums.DiscountType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Apply Discount DTO", description = "Buyurtmaga chegirma qo'llash uchun ma'lumot")
public class ApplyDiscountDTO {

    @NotNull(message = "Discount type is required")
    @Schema(description = "Chegirma turi", example = "PERCENTAGE", requiredMode = Schema.RequiredMode.REQUIRED)
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value musbat bo'lishi kerak")
    @Schema(description = "Chegirma qiymati (PERCENTAGE uchun foiz, FIXED_AMOUNT uchun summa)", example = "10")
    private BigDecimal discountValue;

    /**
     * PERCENTAGE bo'lsa foiz 100 dan oshmasligini tekshiradi.
     * FIXED_AMOUNT bo'lsa bu tekshiruv o'tkazib yuboriladi (summa asosiy validatsiya
     * servis qatlamida originalTotalSum'ga nisbatan bajariladi).
     */
    @AssertTrue(message = "Foizli chegirma 100 dan oshmasligi kerak")
    @Schema(hidden = true)
    public boolean isPercentageValid() {
        if (discountType != DiscountType.PERCENTAGE || discountValue == null) {
            return true;
        }
        return discountValue.compareTo(BigDecimal.valueOf(100)) <= 0;
    }
}