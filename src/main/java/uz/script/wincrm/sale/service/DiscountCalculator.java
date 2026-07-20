package uz.script.wincrm.sale.service;

import org.springframework.stereotype.Component;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.sale.enums.DiscountType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Chegirma summasini hisoblovchi va validatsiya qiluvchi yagona joy.
 * Faqat originalTotalSum bilan ishlaydi - SaleOrderItem hisob-kitobiga ARALASHMAYDI.
 */
@Component
public class DiscountCalculator {

    /**
     * originalTotalSum'dan discountType/discountValue asosida aniq chegirma summasini hisoblaydi.
     *
     * @return discountAmount (0 <= discountAmount <= originalTotalSum kafolatlanadi)
     */
    public BigDecimal calculate(BigDecimal originalTotalSum, DiscountType type, BigDecimal value) {
        if (originalTotalSum == null || originalTotalSum.signum() <= 0) {
            throw new BadRequestException("Original total sum musbat bo'lishi kerak");
        }
        if (type == null || value == null || value.signum() < 0) {
            throw new BadRequestException("Chegirma turi va qiymati to'g'ri bo'lishi kerak");
        }

        BigDecimal discountAmount;

        if (type == DiscountType.PERCENTAGE) {
            if (value.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new BadRequestException("Foizli chegirma 100 dan oshmasligi kerak");
            }
            discountAmount = originalTotalSum
                    .multiply(value)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = value;
        }

        if (discountAmount.compareTo(originalTotalSum) > 0) {
            throw new BadRequestException(
                    "Chegirma summasi buyurtma summasidan katta bo'lishi mumkin emas");
        }

        return discountAmount;
    }
}