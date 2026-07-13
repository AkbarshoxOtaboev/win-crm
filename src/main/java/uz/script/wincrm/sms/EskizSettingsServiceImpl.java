package uz.script.wincrm.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EskizSettingsServiceImpl implements EskizSettingsService {

    private static final Long SETTINGS_ID = 1L;

    private final EskizSettingsRepository repository;
    private final EskizCredentialEncryptor encryptor;
    private final EskizSettingsMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public EskizSettingsResponse getSettings() {
        return mapper.toResponse(repository.findById(SETTINGS_ID).orElse(null));
    }

    @Override
    @Transactional
    public EskizSettingsResponse saveCredentials(EskizCredentialsDto dto) {
        EskizSettings settings = repository.findById(SETTINGS_ID).orElseGet(() -> {
            EskizSettings s = new EskizSettings();
            s.setId(SETTINGS_ID);
            return s;
        });

        settings.setEmail(dto.getEmail());
        settings.setEncryptedPassword(encryptor.encrypt(dto.getPassword()));
        // Login ma'lumotlari o'zgargani uchun eski tokenni bekor qilamiz —
        // keyingi SMS yuborishda avtomatik qayta login bo'ladi.
        settings.setToken(null);
        settings.setTokenExpiresAt(null);
        settings.setUpdatedAt(LocalDateTime.now());
        // settings.setUpdatedBy(currentUserId()); // loyihangizdagi SecurityAuditorAware orqali to'ldiring

        EskizSettings saved = repository.save(settings);
        log.info("Eskiz.uz login ma'lumotlari yangilandi: {}", dto.getEmail());

        return mapper.toResponse(saved);
    }
}