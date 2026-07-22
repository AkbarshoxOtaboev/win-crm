package uz.script.wincrm.telegram.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClientBalanceView(
        BigDecimal totalPurchase,
        BigDecimal totalPaid,
        BigDecimal totalDebt,
        LocalDateTime lastUpdated
) {
}