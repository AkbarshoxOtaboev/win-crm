package uz.script.wincrm.clients;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Client DTO", description = "Client creation and update request")
public class ClientDTO {

    @NotBlank(message = "Full name is required")
    @Schema(
            description = "Client's full name",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Schema(
            description = "Client phone number",
            example = "+998901234567",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phone;

    @NotBlank(message = "Address is required")
    @Schema(
            description = "Client address",
            example = "Tashkent, Chilonzor district",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String address;
}
