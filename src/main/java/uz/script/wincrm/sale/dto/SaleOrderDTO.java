package uz.script.wincrm.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Comment/Notes about the sale order")
    private String comment;

    @NotNull(message = "Order date is required")
    @Schema(description = "Order date and time", required = true)
    private LocalDateTime orderDate;

    @Schema(description = "Total sum of the sale order")
    @NotNull(message = "Order date is required")
    private BigDecimal totalSum;
}