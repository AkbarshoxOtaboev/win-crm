package uz.script.wincrm.telegram.service;

import uz.script.wincrm.telegram.dto.BotSettingsDTO;
import uz.script.wincrm.telegram.response.BotSettingsResponse;

public interface BotSettingsService {

    /** Yangi bot token registratsiya qiladi yoki mavjudini yangilaydi (bitta faol bot bo'ladi). */
    BotSettingsResponse registerOrUpdate(BotSettingsDTO dto);

    BotSettingsResponse getActiveSettings();

    /** Bot ishga tushirilishi uchun ochiq (decrypted) tokenni qaytaradi — faqat ichki foydalanish uchun. */
    String getDecryptedActiveToken();

    void markConnected(boolean connected);
}