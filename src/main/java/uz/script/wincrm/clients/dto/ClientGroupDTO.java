package uz.script.wincrm.clients.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientGroupDTO {

    @NotBlank(message = "Group name is required")
    private String name;
}