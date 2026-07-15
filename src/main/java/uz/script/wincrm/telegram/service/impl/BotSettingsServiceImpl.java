package uz.script.wincrm.telegram.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.telegram.dto.BotSettingsDTO;
import uz.script.wincrm.telegram.BotSettings;
import uz.script.wincrm.telegram.repository.BotSettingsRepository;
import uz.script.wincrm.telegram.response.BotSettingsResponse;
import uz.script.wincrm.telegram.service.BotSettingsService;
import uz.script.wincrm.telegram.util.BotTokenEncryptor;
import uz.script.wincrm.utils.Status;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BotSettingsServiceImpl implements BotSettingsService {

    private final BotSettingsRepository repository;
    private final BotTokenEncryptor encryptor;

    @Override
    @Auditable(action = AuditAction.UPDATE, entity = "BotSettings")
    public BotSettingsResponse registerOrUpdate(BotSettingsDTO dto) {
        log.info("Registering/updating telegram bot token");

        BotSettings settings = repository.findFirstByActiveTrue()
                .orElseGet(() -> BotSettings.builder()
                        .status(Status.ACTIVE)
                        .active(true)
                        .build());

        String rawToken = dto.getToken();
        String hint = rawToken.length() > 4
                ? "****" + rawToken.substring(rawToken.length() - 4)
                : "****";

        settings.setBotUsername(dto.getBotUsername());
        settings.setEncryptedToken(encryptor.encrypt(rawToken));
        settings.setTokenHint(hint);
        // Token yangilanganda ulanish holati qayta tekshirilishi kerak
        // (Eskiz'dagi kabi: credential yangilanganda "connected" holati reset qilinadi)
        settings.setBotConnected(false);
        settings.setActive(true);

        settings = repository.save(settings);

        return mapToResponse(settings);
    }

    @Override
    public BotSettingsResponse getActiveSettings() {
        BotSettings settings = repository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Telegram bot hali registratsiya qilinmagan"));

        return mapToResponse(settings);
    }

    @Override
    public String getDecryptedActiveToken() {
        BotSettings settings = repository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Telegram bot hali registratsiya qilinmagan"));

        return encryptor.decrypt(settings.getEncryptedToken());
    }

    @Override
    public void markConnected(boolean connected) {
        repository.findFirstByActiveTrue().ifPresent(settings -> {
            settings.setBotConnected(connected);
            repository.save(settings);
        });
    }

    private BotSettingsResponse mapToResponse(BotSettings settings) {
        return BotSettingsResponse.builder()
                .id(settings.getId())
                .botUsername(settings.getBotUsername())
                .maskedToken(settings.getTokenHint())
                .active(settings.isActive())
                .botConnected(settings.isBotConnected())
                .createdAt(settings.getCreatedAt())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}