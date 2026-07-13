package uz.script.wincrm.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class EskizSettingsResponse {

    private boolean configured;
    private String email;          // maskalangan, masalan: a***@gmail.com
    private boolean tokenActive;
    private LocalDateTime tokenExpiresAt;
    private LocalDateTime updatedAt;
}