package uz.script.wincrm.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final RestClient eskizRestClient;
    private final EskizTokenService tokenService;
    private final EskizProperties properties;

    @Override
    public void sendSms(String phoneNumber, String message) {
        String normalizedPhone = normalizePhone(phoneNumber);

        if (normalizedPhone == null) {
            log.warn("SMS yuborilmadi: telefon raqami yaroqsiz - '{}'", phoneNumber);
            return;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("mobile_phone", normalizedPhone);
        form.add("message", message);
        form.add("from", properties.getFrom());

        try {
            eskizRestClient.post()
                    .uri("/message/sms/send")
                    .header("Authorization", "Bearer " + tokenService.getToken())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .toBodilessEntity();

            log.info("SMS yuborildi: {}", normalizedPhone);
        } catch (RestClientException e) {
            log.error("SMS yuborishda xatolik ({}): {}", normalizedPhone, e.getMessage());
            throw new SmsSendException("SMS yuborilmadi: " + e.getMessage(), e);
        }
    }

    /**
     * Telefon raqamini 998XXXXXXXXX formatiga keltiradi.
     * Noto'g'ri formatdagi raqamlar uchun null qaytaradi.
     */
    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }

        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.length() == 9) {
            digits = "998" + digits;
        }

        return (digits.length() == 12 && digits.startsWith("998")) ? digits : null;
    }
}