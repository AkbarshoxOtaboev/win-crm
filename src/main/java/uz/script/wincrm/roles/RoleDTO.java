package uz.script.wincrm.roles;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Role creation dto")
public class RoleDTO {
    @Schema(
            description = "Role name. Use uppercase values (e.g. SUPER_ADMIN, ADMIN)."
    )
    @NotEmpty(message = "Role name cannot be empty")
    private String name;
}
