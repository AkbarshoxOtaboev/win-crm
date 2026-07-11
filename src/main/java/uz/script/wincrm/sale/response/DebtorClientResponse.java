package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(name = "Debtor Client Response", description = "Qarzdor mijoz haqida ma'lumot (admin panel uchun)")
public class DebtorClientResponse {

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Client full name", example = "John Doe")
    private String clientFullName;

    @Schema(description = "Client phone number", example = "998901234567")
    private String phone;

    @Schema(description = "Mijozning barcha buyurtmalari bo'yicha umumiy qarzi", example = "1500000.00")
    private BigDecimal totalDebt;

    @Schema(description = "Mijozning qarzi bor buyurtmalari ro'yxati")
    private List<DebtOrderInfo> orders;

    @Getter
    @Setter
    @Builder
    @Schema(name = "Debt Order Info", description = "Qarzi bor buyurtma haqida qisqacha ma'lumot")
    public static class DebtOrderInfo {

        @Schema(description = "Sale order ID", example = "1")
        private Long saleOrderId;

        @Schema(description = "Buyurtma bo'yicha qarz summasi", example = "500000.00")
        private BigDecimal debtSum;

        @Schema(description = "Buyurtma sanasi")
        private LocalDateTime orderDate;

        @Schema(description = "Buyurtmaga biriktirilgan xodim (User) ID'si", example = "3")
        private Long userId;

        @Schema(description = "Buyurtmaga biriktirilgan xodimning to'liq ismi", example = "Ali Valiyev")
        private String userFullName;
    }
}