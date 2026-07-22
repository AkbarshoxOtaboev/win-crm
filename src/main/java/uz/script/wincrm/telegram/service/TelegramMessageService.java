package uz.script.wincrm.telegram.service;

import uz.script.wincrm.telegram.dto.TelegramClientMessageDTO;

public interface TelegramMessageService {

    /** Tanlangan mijozga (clientId orqali) Telegram bot orqali erkin xabar yuboradi. */
    void sendToClient(TelegramClientMessageDTO dto);
}