package uz.script.wincrm.clients.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Client Response", description = "Client information returned by the API")
public class ClientResponse {

    @Schema(
            description = "Unique client identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Client full name",
            example = "John Doe"
    )
    private String fullName;

    @Schema(
            description = "Client INN (Tax Identification Number)",
            example = "306123456"
    )
    private String inn;

    @Schema(
            description = "Primary phone number",
            example = "+998901234567"
    )
    private String phone;

    @Schema(
            description = "Additional phone number",
            example = "+998911234567"
    )
    private String additionalPhone;

    @Schema(
            description = "Client address",
            example = "Tashkent, Chilonzor district"
    )
    private String address;

    @Schema(
            description = "Bank name",
            example = "Agrobank"
    )
    private String bankName;

    @Schema(
            description = "Bank MFO",
            example = "00450"
    )
    private String mfo;

    @Schema(
            description = "Bank account number",
            example = "20208000123456789001"
    )
    private String accountNumber;

    @Schema(
            description = "Additional description about the client",
            example = "Regular customer with long-term contract"
    )
    private String description;

    @Schema(
            description = "Current client status",
            example = "ACTIVE",
            implementation = Status.class
    )
    private Status status;

    @Schema(
            description = "Date and time when the client was created",
            example = "2026-07-04T09:30:15"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the client was last updated",
            example = "2026-07-04T11:45:30"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "Username of the user who created the client",
            example = "admin"
    )
    private String createdUsername;

    private Long clientGroupId;
    private String clientGroupName;
}