package uz.script.wincrm.roles;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Role response")
public class RoleResponse {
    private Long id;
    private String name;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private String createdUsername;

    public RoleResponse(Long id, String name, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, String createdUsername) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdUsername = createdUsername;
    }
}
