package uz.script.wincrm.clients.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.clients.enums.NoteType;
import uz.script.wincrm.clients.enums.ReminderStatus;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Client Note Response", description = "Mijoz bilan bo'lgan suhbat/eslatma ma'lumotlari (javob)")
public class ClientNoteResponse {

    @Schema(description = "Unique note identifier", example = "1")
    private Long id;

    @Schema(description = "Mijoz identifikatori", example = "1")
    private Long clientId;

    @Schema(description = "Mijoz to'liq ismi", example = "John Doe")
    private String clientFullName;

    @Schema(description = "Bog'liq sotuv buyurtmasi id", example = "15")
    private Long saleOrderId;

    @Schema(description = "Suhbat/eslatma turi", example = "PAYMENT_PROMISE")
    private NoteType type;

    @Schema(description = "Suhbat mazmuni", example = "Qarzni 3 kundan keyin beraman dedi")
    private String content;

    @Schema(description = "Suhbat/qo'ng'iroq bo'lgan sana va vaqt", example = "2026-07-18T14:30:00")
    private LocalDateTime interactionDate;

    @Schema(description = "Eslatish kerak bo'lgan sana", example = "2026-07-21")
    private LocalDate reminderDate;

    @Schema(description = "Eslatma holati", example = "PENDING")
    private ReminderStatus reminderStatus;

    @Schema(description = "Va'da qilingan to'lov summasi", example = "1500000")
    private BigDecimal promisedAmount;

    @Schema(description = "Yozuv holati", example = "ACTIVE", implementation = Status.class)
    private Status status;

    @Schema(description = "Yaratilgan sana va vaqt", example = "2026-07-18T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Oxirgi yangilangan sana va vaqt", example = "2026-07-18T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Yozuvni yaratgan foydalanuvchi", example = "admin")
    private String createdUsername;
}