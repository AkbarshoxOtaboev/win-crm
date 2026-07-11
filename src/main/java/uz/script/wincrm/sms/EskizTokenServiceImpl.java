package uz.script.wincrm.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

/**
 * Eskiz.uz tokeni ~30 kun amal qiladi. Har SMS uchun qayta login qilmaslik
 * uchun tokenni xotirada keshlaymiz va muddati tugashiga yaqinlashganda
 * avtomatik yangilaymiz.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EskizTokenServiceImpl implements EskizTokenService {

    private final RestClient eskizRestClient;
    private final EskizProperties properties;

    private volatile String cachedToken;
    private volatile LocalDateTime tokenExpiresAt;

    @Override
    public synchronized String getToken() {
        if (cachedToken != null && tokenExpiresAt != null && LocalDateTime.now().isBefore(tokenExpiresAt)) {
            return cachedToken;
        }
        return login();
    }

    private String login() {
        log.info("Eskiz.uz'ga login qilinmoqda");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("email", properties.getEmail());
        form.add("password", properties.getPassword());

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

            cachedToken = response.getData().getToken();
            // Eskiz tokeni ~30 kun amal qiladi, ehtiyot uchun 29 kun deb hisoblaymiz
            tokenExpiresAt = LocalDateTime.now().plusDays(29);

            return cachedToken;
        } catch (RestClientException e) {
            log.error("Eskiz.uz'ga login qilishda xatolik: {}", e.getMessage());
            throw new SmsSendException("Eskiz.uz'ga ulanib bo'lmadi: " + e.getMessage(), e);
        }
    }
}