package uz.script.wincrm.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Warehouse DTO", description = "Warehouse creation and update request")
public class WarehouseDTO {

    @NotBlank(message = "Name is required")
    @Schema(
            description = "Warehouse name",
            example = "Central Warehouse",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}
