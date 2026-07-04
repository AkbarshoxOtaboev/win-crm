package uz.script.wincrm.clients;
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
            description = "Client's full name",
            example = "John Doe"
    )
    private String fullName;

    @Schema(
            description = "Client phone number",
            example = "+998901234567"
    )
    private String phone;

    @Schema(
            description = "Client address",
            example = "Tashkent, Chilonzor district"
    )
    private String address;

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
}
