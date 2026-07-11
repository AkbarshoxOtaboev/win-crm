package uz.script.wincrm.sms;

public interface EskizTokenService {

    /**
     * Amaldagi (keshdagi yoki yangi login qilib olingan) tokenni qaytaradi.
     */
    String getToken();
}