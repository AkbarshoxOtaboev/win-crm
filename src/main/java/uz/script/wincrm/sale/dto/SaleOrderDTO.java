package uz.script.wincrm.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SaleOrder DTO", description = "Sale Order data transfer object for creating and updating")
public class SaleOrderDTO {

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @NotNull(message = "Warehouse ID is required")
    @Schema(description = "Warehouse ID", example = "1", required = true)
    private Long warehouseId;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID", example = "1", required = true)
    private Long userId;

    @Schema(description = "Comment/Notes about the sale order")
    private String comment;

    @NotNull(message = "Order date is required")
    @Schema(description = "Order date and time", required = true)
    private LocalDateTime orderDate;

    @Schema(
            description = "Buyurtma tayyor bo'lishi rejalashtirilgan sana (ixtiyoriy)",
            example = "2026-07-17T00:00:00"
    )
    private LocalDateTime plannedReadyDate;

    @Schema(
            description = "Mijozga yetkazilishi rejalashtirilgan sana (ixtiyoriy). " +
                    "Buyurtma vaqtida yetkazilyaptimi nazorati shu sana asosida yuritiladi.",
            example = "2026-07-18T00:00:00"
    )
    private LocalDateTime plannedDeliveryDate;

    @Schema(description = "Total sum of the sale order")
    @NotNull(message = "Order date is required")
    private BigDecimal totalSum;

    @AssertTrue(message = "Planned ready date orderDate'dan oldin bo'lishi mumkin emas")
    @Schema(hidden = true)
    public boolean isPlannedReadyDateValid() {
        if (plannedReadyDate == null || orderDate == null) {
            return true;
        }
        return !plannedReadyDate.isBefore(orderDate);
    }

    @AssertTrue(message = "Planned delivery date plannedReadyDate'dan oldin bo'lishi mumkin emas")
    @Schema(hidden = true)
    public boolean isPlannedDeliveryDateValid() {
        if (plannedDeliveryDate == null || plannedReadyDate == null) {
            return true;
        }
        return !plannedDeliveryDate.isBefore(plannedReadyDate);
    }
}