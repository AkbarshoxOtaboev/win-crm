package uz.script.wincrm.suppliers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Supplier create/update request")
public class SupplierDTO {

    @NotBlank(message = "Supplier name is required")
    @Schema(description = "Supplier name", example = "Artel")
    private String name;

    @Size(max = 20)
    @Schema(description = "INN (STIR)", example = "305987654")
    private String inn;


    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^\\+?[0-9]{9,15}$",
            message = "Phone number is invalid"
    )
    @Schema(description = "Phone number", example = "+998901234567")
    private String phone;

    @Pattern(
            regexp = "^$|^\\+?[0-9]{9,15}$",
            message = "Additional phone number is invalid"
    )
    @Schema(description = "Additional phone")
    private String additionalPhone;



    @Schema(description = "Address")
    private String address;

    @Schema(description = "Bank name")
    private String bankName;

    @Schema(description = "MFO", example = "00450")
    private String mfo;

    @Schema(description = "Account number")
    private String accountNumber;

    @Schema(description = "Description")
    private String description;
}