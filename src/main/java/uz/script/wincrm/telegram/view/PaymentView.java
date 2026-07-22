package uz.script.wincrm.telegram.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Telegram bot uchun Payment'ning yengil proyeksiyasi — paymentType.name va
 * saleOrder.id allaqachon JOIN orqali olib kelingan, botda qo'shimcha
 * lazy-load kerak emas.
 */
public record PaymentView(
        Long id,
        LocalDateTime paymentDate,
        BigDecimal paymentAmount,
        String paymentTypeName,
        Long saleOrderId,
        String comment
) {
}