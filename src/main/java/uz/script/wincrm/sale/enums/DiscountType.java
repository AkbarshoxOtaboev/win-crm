package uz.script.wincrm.sale.enums;

public enum DiscountType {

    /**
     * Foizli chegirma. discountValue 0..100 oralig'ida bo'lishi kerak.
     * discountAmount = originalTotalSum * discountValue / 100
     */
    PERCENTAGE,

    /**
     * Aniq summa (pul birligida) chegirmasi.
     * discountAmount = discountValue (originalTotalSum'dan katta bo'lmasligi kerak)
     */
    FIXED_AMOUNT
}