package uz.script.wincrm.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eskiz")
@Getter
@Setter
public class EskizProperties {

    /**
     * Eslatma: email va parol endi bu yerda emas — ular dashboard orqali
     * kiritiladi va "eskiz_settings" jadvalida shifrlangan holda saqlanadi.
     */
    private String baseUrl = "https://notify.eskiz.uz/api";
    private String from = "4546";
    private String encryptionSecret;
}