package uz.script.wincrm.inventory.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.inventory.enums.InventoryCheckStatus;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(name = "Inventory Check Response", description = "Inventarizatsiya hujjati ma'lumoti")
public class InventoryCheckResponse {

    @Schema(description = "Inventarizatsiya identifikatori", example = "1")
    private Long id;

    @Schema(description = "Ombor identifikatori", example = "1")
    private Long warehouseId;

    @Schema(description = "Ombor nomi", example = "Central Warehouse")
    private String warehouseName;

    @Schema(description = "Inventarizatsiya holati", example = "IN_PROGRESS", implementation = InventoryCheckStatus.class)
    private InventoryCheckStatus checkStatus;

    @Schema(description = "Izoh", example = "Yillik inventarizatsiya")
    private String comment;

    @Schema(description = "Tasdiqlangan vaqt", example = "2026-07-18T14:20:00")
    private LocalDateTime confirmedAt;

    @Schema(description = "Tasdiqlagan foydalanuvchi", example = "admin")
    private String confirmedUsername;

    @Schema(description = "Qatorlar ro'yxati")
    private List<InventoryCheckItemResponse> items;

    @Schema(description = "Yozuv holati (soft-delete)", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Yaratilgan vaqt", example = "2026-07-18T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Yaratgan foydalanuvchi", example = "admin")
    private String createdUsername;
}