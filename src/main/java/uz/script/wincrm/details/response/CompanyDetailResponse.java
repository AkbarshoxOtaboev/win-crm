package uz.script.wincrm.details.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "CompanyDetail Response", description = "Company detail information returned by the API")
public class CompanyDetailResponse {

    @Schema(description = "Unique company detail identifier", example = "1")
    private Long id;

    @Schema(description = "Company name", example = "Wincrm Group MChJ")
    private String companyName;

    @Schema(description = "Tax identification number (INN)", example = "123456789")
    private String inn;

    @Schema(description = "Economic activity code (OKED)", example = "62010")
    private String oked;

    @Schema(description = "Bank code (MFO)", example = "00450")
    private String mfo;

    @Schema(description = "Bank account number", example = "20208000123456789012")
    private String accountNumber;

    @Schema(description = "Bank name", example = "Ipoteka Bank")
    private String bankName;

    @Schema(description = "Director's full name", example = "Aliyev Ali")
    private String director;

    @Schema(description = "Phone number", example = "+998901234567")
    private String phone;

    @Schema(description = "Email address", example = "info@wincrm.uz")
    private String email;

    @Schema(description = "Address", example = "Tashkent, Chilonzor tumani")
    private String address;

    @Schema(description = "Description/notes")
    private String description;

    @Schema(description = "Current status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the record was created", example = "2026-07-09T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the record was last updated", example = "2026-07-09T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user who created the record", example = "1")
    private Long createdBy;
}