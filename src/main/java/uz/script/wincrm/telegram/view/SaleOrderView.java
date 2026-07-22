package uz.script.wincrm.telegram.view;

import uz.script.wincrm.sale.enums.SalesOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Telegram bot uchun SaleOrder'ning yengil (lazy proxy'siz) proyeksiyasi.
 * TelegramBotDataService ichida JPQL constructor-expression orqali to'ldiriladi.
 */
public record SaleOrderView(
        Long id,
        LocalDateTime orderDate,
        BigDecimal totalSum,
        BigDecimal paidSum,
        BigDecimal debtSum,
        SalesOrderStatus salesOrderStatus
) {
}