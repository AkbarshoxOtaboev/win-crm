package uz.script.wincrm.sms;

import lombok.Getter;
import lombok.Setter;

/**
 * Eskiz.uz "/auth/login" javobi:
 * { "message": "token_generated", "data": { "token": "..." }, "token_type": "bearer" }
 */
@Getter
@Setter
public class EskizAuthResponse {

    private String message;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String token;
    }
}