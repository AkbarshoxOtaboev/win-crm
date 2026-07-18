package uz.script.wincrm.inventory.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Start Inventory Check Request", description = "Ombor uchun yangi inventarizatsiya boshlash so'rovi")
public class StartInventoryCheckRequest {

    @NotNull(message = "warehouseId bo'sh bo'lishi mumkin emas")
    @Schema(description = "Inventarizatsiya o'tkaziladigan ombor identifikatori", example = "1")
    private Long warehouseId;

    @Schema(description = "Ixtiyoriy izoh", example = "Yillik inventarizatsiya")
    private String comment;
}