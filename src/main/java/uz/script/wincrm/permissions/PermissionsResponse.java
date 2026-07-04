package uz.script.wincrm.permissions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Permission response object")
public class PermissionsResponse {
    @Schema(
            description = "Unique permission ID",
            example = "1"
    )
    private Long id;
    @Schema(
            description = "Permission name",
            example = "USER_CREATE"
    )
    private String name;
}
