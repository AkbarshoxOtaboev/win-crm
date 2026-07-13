package uz.script.wincrm.sms;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EskizSettingsMapper {

    public EskizSettingsResponse toResponse(EskizSettings settings) {
        if (settings == null || settings.getEmail() == null) {
            return EskizSettingsResponse.builder()
                    .configured(false)
                    .tokenActive(false)
                    .build();
        }

        boolean tokenActive = settings.getToken() != null
                && settings.getTokenExpiresAt() != null
                && settings.getTokenExpiresAt().isAfter(LocalDateTime.now());

        return EskizSettingsResponse.builder()
                .configured(true)
                .email(maskEmail(settings.getEmail()))
                .tokenActive(tokenActive)
                .tokenExpiresAt(settings.getTokenExpiresAt())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 1) {
            return "***" + email.substring(at);
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}