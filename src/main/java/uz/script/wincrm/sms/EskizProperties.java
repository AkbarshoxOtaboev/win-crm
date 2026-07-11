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
     * application.yml:
     * eskiz:
     *   base-url: https://notify.eskiz.uz/api
     *   email: ${ESKIZ_EMAIL}
     *   password: ${ESKIZ_PASSWORD}
     *   from: 4546
     */
    private String baseUrl = "https://notify.eskiz.uz/api";
    private String email;
    private String password;
    private String from = "4546";
}