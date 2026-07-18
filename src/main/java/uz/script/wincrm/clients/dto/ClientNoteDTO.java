package uz.script.wincrm.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.clients.enums.NoteType;
import uz.script.wincrm.clients.enums.ReminderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "Client Note Request", description = "Mijoz bilan bo'lgan suhbat/eslatma ma'lumotlari")
public class ClientNoteDTO {

    @NotNull(message = "Client id is required")
    @Schema(description = "Mijoz identifikatori", example = "1")
    private Long clientId;

    @Schema(description = "Bog'liq sotuv buyurtmasi id (ixtiyoriy)", example = "15")
    private Long saleOrderId;

    @NotNull(message = "Note type is required")
    @Schema(description = "Suhbat/eslatma turi", example = "PAYMENT_PROMISE")
    private NoteType type;

    @NotBlank(message = "Content is required")
    @Schema(description = "Suhbat mazmuni", example = "Qarzni 3 kundan keyin beraman dedi")
    private String content;

    @Schema(description = "Suhbat/qo'ng'iroq bo'lgan sana va vaqt", example = "2026-07-18T14:30:00")
    private LocalDateTime interactionDate;

    @Schema(description = "Eslatish kerak bo'lgan sana", example = "2026-07-21")
    private LocalDate reminderDate;

    @Schema(description = "Eslatma holati (odatda create paytida bo'sh qoldiriladi)", example = "PENDING")
    private ReminderStatus reminderStatus;

    @Positive(message = "Promised amount must be positive")
    @Schema(description = "Va'da qilingan to'lov summasi", example = "1500000")
    private BigDecimal promisedAmount;
}