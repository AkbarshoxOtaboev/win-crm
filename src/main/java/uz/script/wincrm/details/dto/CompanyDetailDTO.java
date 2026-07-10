package uz.script.wincrm.details.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CompanyDetail DTO", description = "Company detail data transfer object for creating and updating")
public class CompanyDetailDTO {

    @NotBlank(message = "Company name is required")
    @Schema(description = "Company name", example = "Wincrm Group MChJ", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    @NotBlank(message = "INN is required")
    @Schema(description = "Tax identification number (INN)", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "info@wincrm.uz")
    private String email;

    @Schema(description = "Address", example = "Tashkent, Chilonzor tumani")
    private String address;

    @Schema(description = "Description/notes")
    private String description;
}