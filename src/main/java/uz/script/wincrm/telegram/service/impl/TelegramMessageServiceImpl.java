package uz.script.wincrm.telegram.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.clients.repository.ClientRepository;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.telegram.TelegramSendResult;
import uz.script.wincrm.telegram.config.TelegramBotLifecycleService;
import uz.script.wincrm.telegram.dto.TelegramClientMessageDTO;
import uz.script.wincrm.telegram.service.TelegramMessageService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TelegramMessageServiceImpl implements TelegramMessageService {

    private final TelegramBotLifecycleService lifecycleService;
    private final ClientRepository clientRepository;

    @Override
    @Auditable(action = AuditAction.CREATE, entity = "TelegramMessage")
    public void sendToClient(TelegramClientMessageDTO dto) {
        if (!clientRepository.existsById(dto.getClientId())) {
            throw new ResourceNotFoundException("Mijoz topilmadi: id=" + dto.getClientId());
        }

        TelegramSendResult result = lifecycleService.sendMessageToClient(dto.getClientId(), dto.getMessage());

        switch (result) {
            case SENT -> log.info("Telegram xabar muvaffaqiyatli yuborildi: clientId={}", dto.getClientId());
            case BOT_NOT_CONNECTED -> throw new IllegalStateException(
                    "Telegram bot hozircha ulanmagan. Admin panelidan bot tokenini tekshiring.");
            case CLIENT_NOT_LINKED -> throw new IllegalStateException(
                    "Bu mijoz Telegram botdan hali ro'yxatdan o'tmagan (telefon raqami orqali /start bosilmagan).");
            case SEND_FAILED -> throw new IllegalStateException(
                    "Xabar yuborishda Telegram API xatoligi yuz berdi. Qaytadan urinib ko'ring.");
        }
    }
}