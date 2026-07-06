package uz.script.wincrm.suppliers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Data
@Schema(description = "Supplier filter request")
public class SupplierFilterDTO {

    @Schema(
            description = "Supplier name",
            example = "Artel"
    )
    private String name;

    @Schema(
            description = "INN (STIR)",
            example = "305123456"
    )
    private String inn;

    @Schema(
            description = "Phone number",
            example = "+998901234567"
    )
    private String phone;

    @Schema(
            description = "Supplier status",
            example = "ACTIVE"
    )
    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(
            description = "Created from date and time",
            example = "2026-01-01T00:00:00"
    )
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(
            description = "Created to date and time",
            example = "2026-12-31T23:59:59"
    )
    private LocalDateTime toDate;
}