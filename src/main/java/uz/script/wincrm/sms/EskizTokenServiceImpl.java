package uz.script.wincrm.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

/**
 * Login ma'lumotlari endi application.yml'da emas — dashboard orqali
 * kiritilib "eskiz_settings" jadvalida shifrlangan holda saqlanadi.
 * Token ham shu jadvalda saqlanadi, shuning uchun ilova qayta ishga
 * tushganda ham qayta login qilish shart bo'lmaydi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EskizTokenServiceImpl implements EskizTokenService {

    private static final Long SETTINGS_ID = 1L;

    private final RestClient eskizRestClient;
    private final EskizSettingsRepository settingsRepository;
    private final EskizCredentialEncryptor encryptor;

    @Override
    @Transactional
    public synchronized String getToken() {
        EskizSettings settings = settingsRepository.findById(SETTINGS_ID)
                .orElseThrow(() -> new SmsSendException(
                        "Eskiz.uz login ma'lumotlari kiritilmagan. Dashboard orqali email/parolni kiriting."));

        if (settings.getToken() != null
                && settings.getTokenExpiresAt() != null
                && LocalDateTime.now().isBefore(settings.getTokenExpiresAt())) {
            return settings.getToken();
        }

        return login(settings);
    }

    private String login(EskizSettings settings) {
        log.info("Eskiz.uz'ga login qilinmoqda: {}", settings.getEmail());

        String rawPassword = encryptor.decrypt(settings.getEncryptedPassword());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("email", settings.getEmail());
        form.add("password", rawPassword);

        try {
            EskizAuthResponse response = eskizRestClient.post()
                    .uri("/auth/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(EskizAuthResponse.class);

            if (response == null || response.getData() == null || response.getData().getToken() == null) {
                throw new SmsSendException("Eskiz.uz'dan token olinmadi (bo'sh javob)");
            }

            String token = response.getData().getToken();
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(29);

            settings.setToken(token);
            settings.setTokenExpiresAt(expiresAt);
            settingsRepository.save(settings);

            return token;
        } catch (RestClientException e) {
            log.error("Eskiz.uz'ga login qilishda xatolik: {}", e.getMessage());
            throw new SmsSendException("Eskiz.uz'ga ulanib bo'lmadi: " + e.getMessage(), e);
        }
    }
}