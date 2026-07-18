package uz.script.wincrm.inventory.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Update Inventory Check Item Request", description = "Sanoq natijasida aniqlangan haqiqiy miqdorni kiritish/yangilash")
public class UpdateInventoryCheckItemRequest {

    @NotNull(message = "actualCount bo'sh bo'lishi mumkin emas")
    @DecimalMin(value = "0", message = "actualCount manfiy bo'lishi mumkin emas")
    @Schema(description = "Jismonan sanalgan haqiqiy miqdor", example = "48")
    private BigDecimal actualCount;

    @Schema(description = "Farq sababi haqida izoh", example = "2 dona shikastlangan holda topildi")
    private String comment;
}