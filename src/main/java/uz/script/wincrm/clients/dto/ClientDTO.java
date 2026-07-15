package uz.script.wincrm.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Client DTO", description = "Client creation and update request")
public class ClientDTO {

    @NotBlank(message = "Full name is required")
    @Schema(
            description = "Client full name",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fullName;

    @Size(max = 20, message = "INN must not exceed 20 characters")
    @Schema(
            description = "Client INN (Tax Identification Number)",
            example = "306123456",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String inn;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Schema(
            description = "Primary phone number",
            example = "+998901234567",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phone;

    @Size(max = 20, message = "Additional phone number must not exceed 20 characters")
    @Schema(
            description = "Additional phone number",
            example = "+998911234567",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String additionalPhone;

    @NotBlank(message = "Address is required")
    @Schema(
            description = "Client address",
            example = "Tashkent, Chilonzor district",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String address;

    @Size(max = 150, message = "Bank name must not exceed 150 characters")
    @Schema(
            description = "Bank name",
            example = "Agrobank",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String bankName;

    @Size(max = 10, message = "MFO must not exceed 10 characters")
    @Schema(
            description = "Bank MFO",
            example = "00450",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String mfo;

    @Size(max = 30, message = "Account number must not exceed 30 characters")
    @Schema(
            description = "Bank account number",
            example = "20208000123456789001",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String accountNumber;

    @Schema(
            description = "Additional description about the client",
            example = "Regular customer with long-term contract",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String description;

    @Schema(
            description = "Client group id",
            example = "1"
    )
    private Long clientGroupId;
}