package uz.script.wincrm.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * ClientBalance odatda avtomatik hisoblanadi (SaleOrder va Payment yaratilganda).
 * Bu DTO faqat admin tomonidan qo'lda tuzatish (manual adjustment) kerak bo'lganda ishlatiladi,
 * masalan ma'lumotlar sinxronizatsiyadan chiqib qolganda.
 */
@Getter
@Setter
@Schema(name = "Client Balance DTO", description = "Client balance manual adjustment request")
public class ClientBalanceDTO {

    @NotNull(message = "Total purchase is required")
    @DecimalMin(value = "0.0", message = "Total purchase cannot be negative")
    @Schema(
            description = "Total purchase amount",
            example = "15000000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal totalPurchase;

    @NotNull(message = "Total paid is required")
    @DecimalMin(value = "0.0", message = "Total paid cannot be negative")
    @Schema(
            description = "Total paid amount",
            example = "10000000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal totalPaid;
}