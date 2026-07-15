package uz.script.wincrm.dashboard.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyExpenseReportResponse {

    private Long categoryId;

    private String categoryName;

    private BigDecimal totalAmount;
}
