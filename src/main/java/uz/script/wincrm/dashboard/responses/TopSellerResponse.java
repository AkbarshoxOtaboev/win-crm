package uz.script.wincrm.dashboard.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Top Seller Response", description = "Sotuvchi (user) bo'yicha jamlangan savdo statistikasi")
public class TopSellerResponse {

    @Schema(description = "Sotuvchi (user) ID", example = "3")
    private Long userId;

    @Schema(description = "Sotuvchi to'liq ismi", example = "Akbar Aliyev")
    private String userName;

    @Schema(description = "Shu davrda amalga oshirilgan buyurtmalar soni", example = "42")
    private Long orderCount;

    @Schema(description = "Shu davrdagi umumiy savdo summasi (totalSum yig'indisi)", example = "128400000.00")
    private BigDecimal totalAmount;

    public TopSellerResponse(Long userId, String userName, Long orderCount, BigDecimal totalAmount) {
        this.userId = userId;
        this.userName = userName;
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
    }
}