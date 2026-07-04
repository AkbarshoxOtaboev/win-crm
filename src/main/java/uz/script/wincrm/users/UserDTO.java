package uz.script.wincrm.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


import java.util.Set;

@Getter
@Setter
@Schema(description = "User creation dto")
public class UserDTO {
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;
    @NotEmpty(message = "Phone cannot be empty")
    private String phone;
    @NotEmpty(message = "Role IDs cannot be empty")
    private Set<Long> roleIds;
    private MultipartFile photo;
}
