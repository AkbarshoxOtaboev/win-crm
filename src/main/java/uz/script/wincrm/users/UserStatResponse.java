package uz.script.wincrm.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Schema(name = "User Stat Response", description = "User (salesperson) sale orders and payments statistics")
public class UserStatResponse {

    @Schema(description = "User identifier", example = "1")
    private Long userId;

    @Schema(description = "User full name", example = "John Doe")
    private String userFullName;

    @Schema(description = "Total number of sale orders created by this user", example = "128")
    private Long totalOrdersCount;

    @Schema(description = "Total sum of all sale orders created by this user (SaleOrder.totalSum)", example = "45000000")
    private BigDecimal totalOrdersSum;

    @Schema(description = "Total sum of all payments processed by this user (Payment.paymentAmount)", example = "38000000")
    private BigDecimal totalPaidSum;

    @Schema(description = "Debt amount (totalOrdersSum - totalPaidSum)", example = "7000000")
    private BigDecimal totalDebt;
}