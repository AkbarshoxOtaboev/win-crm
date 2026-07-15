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
@Schema(
        name = "Client Group Response",
        description = "Client group information returned by the API"
)
public class ClientGroupResponse {

    @Schema(
            description = "Unique client group identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Client group name",
            example = "VIP Clients"
    )
    private String name;

    @Schema(
            description = "Current client group status",
            example = "ACTIVE",
            implementation = Status.class
    )
    private Status status;

    @Schema(
            description = "Date and time when the client group was created",
            example = "2026-07-14T10:30:15"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the client group was last updated",
            example = "2026-07-14T14:45:30"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "Username of the user who created the client group",
            example = "admin"
    )
    private String createdUsername;
}