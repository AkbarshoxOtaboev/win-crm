package uz.script.wincrm.warehouse.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Warehouse Response", description = "Warehouse information returned by the API")
public class WarehouseResponse {

    @Schema(description = "Unique warehouse identifier", example = "1")
    private Long id;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    private String name;

    @Schema(description = "Current warehouse status", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Date and time when the warehouse was created", example = "2026-07-04T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the warehouse was last updated", example = "2026-07-04T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Username of the user who created the warehouse", example = "admin")
    private String createdUsername;
}
