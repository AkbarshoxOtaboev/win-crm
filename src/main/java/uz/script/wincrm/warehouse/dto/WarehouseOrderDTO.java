package uz.script.wincrm.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "Warehouse Order DTO", description = "Warehouse order creation and update request")
public class WarehouseOrderDTO {

    @NotNull(message = "Supplier id is required")
    @Schema(
            description = "Supplier identifier",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long supplierId;

    @NotNull(message = "Warehouse id is required")
    @Schema(
            description = "Warehouse identifier",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long warehouseId;

    @Schema(
            description = "Optional comment about the order",
            example = "Urgent delivery, handle with care"
    )
    private String comment;

    @NotNull(message = "Arrival date is required")
    @Schema(
            description = "Date and time the goods arrived / are expected to arrive",
            example = "2026-07-10T14:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime arrivalDate;
}
