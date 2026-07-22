package uz.script.wincrm.telegram.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.script.wincrm.clients.repository.ClientRepository;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.telegram.TelegramSendResult;
import uz.script.wincrm.telegram.TelegramUser;
import uz.script.wincrm.telegram.bot.CrmTelegramBot;
import uz.script.wincrm.telegram.keyboard.TelegramKeyboards;
import uz.script.wincrm.telegram.repository.TelegramUserRepository;
import uz.script.wincrm.telegram.response.BotSettingsResponse;
import uz.script.wincrm.telegram.service.BotSettingsService;
import uz.script.wincrm.telegram.service.TelegramBotDataService;
import uz.script.wincrm.telegram.session.BotSessionService;

import java.util.Optional;

/**
 * Telegram botni ishga tushirish/qayta ulash va admin panelidan tanlangan
 * mijozga xabar yuborish uchun markaziy servis.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotLifecycleService {

    private final BotSettingsService botSettingsService;
    private final TelegramUserRepository telegramUserRepository;
    private final ClientRepository clientRepository;
    private final TelegramKeyboards keyboards;
    private final BotSessionService sessionService;
    private final TelegramBotDataService botDataService;

    private TelegramBotsApi telegramBotsApi;
    private BotSession activeBotSession;
    private CrmTelegramBot activeBot;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        startBot();
    }

    public synchronized void startBot() {
        stopBotIfRunning();

        Optional<String> tokenOpt = safeGetDecryptedToken();
        if (tokenOpt.isEmpty()) {
            log.warn("Telegram bot tokeni bazada topilmadi. Bot ishga tushirilmadi. " +
                    "Admin panelidan POST /api/telegram/bot-settings/register orqali token kiriting.");
            return;
        }

        try {
            BotSettingsResponse settings = botSettingsService.getActiveSettings();

            CrmTelegramBot bot = new CrmTelegramBot(
                    tokenOpt.get(),
                    settings.getBotUsername(),
                    telegramUserRepository,
                    clientRepository,
                    keyboards,
                    sessionService,
                    botDataService
            );

            if (telegramBotsApi == null) {
                telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            }

            activeBotSession = telegramBotsApi.registerBot(bot);
            activeBot = bot;
            botSettingsService.markConnected(true);
            log.info("Telegram bot muvaffaqiyatli ulandi: {}", settings.getBotUsername());
        } catch (Exception e) {
            activeBot = null;
            botSettingsService.markConnected(false);
            log.error("Telegram botni ulashda xatolik. Token yoki tarmoq sozlamalarini tekshiring.", e);
        }
    }

    public synchronized void stopBot() {
        stopBotIfRunning();
        botSettingsService.markConnected(false);
    }

    /**
     * CRM admin panelidan tanlangan mijozga bot orqali erkin xabar yuboradi.
     * Mijoz botdan avval telefon raqami orqali ro'yxatdan o'tgan bo'lishi shart
     * (TelegramUser.client bog'langan bo'lishi kerak) — aks holda CLIENT_NOT_LINKED qaytadi.
     */
    public synchronized TelegramSendResult sendMessageToClient(Long clientId, String text) {
        if (activeBot == null) {
            return TelegramSendResult.BOT_NOT_CONNECTED;
        }

        Optional<TelegramUser> userOpt = telegramUserRepository.findByClient_Id(clientId);
        if (userOpt.isEmpty()) {
            return TelegramSendResult.CLIENT_NOT_LINKED;
        }

        boolean sent = activeBot.sendMessageToClientChat(userOpt.get().getChatId(), text);
        return sent ? TelegramSendResult.SENT : TelegramSendResult.SEND_FAILED;
    }

    private void stopBotIfRunning() {
        if (activeBotSession != null) {
            try {
                if (activeBotSession.isRunning()) {
                    activeBotSession.stop();
                }
            } catch (Exception e) {
                log.warn("Eski Telegram bot sessiyasini to'xtatishda xatolik (e'tiborsiz qoldiriladi)", e);
            } finally {
                activeBotSession = null;
                activeBot = null;
            }
        }
    }

    private Optional<String> safeGetDecryptedToken() {
        try {
            return Optional.of(botSettingsService.getDecryptedActiveToken());
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
}