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
import uz.script.wincrm.telegram.bot.CrmTelegramBot;
import uz.script.wincrm.telegram.keyboard.TelegramKeyboards;
import uz.script.wincrm.telegram.repository.TelegramUserRepository;
import uz.script.wincrm.telegram.response.BotSettingsResponse;
import uz.script.wincrm.telegram.service.BotSettingsService;
import uz.script.wincrm.telegram.session.BotSessionService;

import java.util.Optional;

/**
 * Telegram botni ishga tushirish/qayta ulash uchun markaziy servis.
 * <p>
 * MUHIM XATTI-HARAKAT: bazada hali bot tokeni bo'lmasa, {@link #startBot()}
 * shunchaki ogohlantirish log yozadi va jim qaytadi — {@code CrmTelegramBot}
 * hech qachon Spring bean sifatida avtomatik yaratilmaydi, shuning uchun
 * ilova (CRM backend) tokensiz ham muammosiz ishga tushadi.
 * <p>
 * Admin panelidan {@code POST /api/telegram/bot-settings/register} orqali
 * token saqlangandan so'ng,
 * {@link uz.script.wincrm.telegram.controller.BotSettingsController}
 * shu servisning {@link #startBot()} metodini chaqirib, botni darhol (ilovani
 * qayta ishga tushirmasdan) ulaydi.
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

    private TelegramBotsApi telegramBotsApi;
    private BotSession activeBotSession;

    /**
     * Ilova to'liq ko'tarilgandan so'ng (barcha bean'lar tayyor bo'lgach)
     * bir marta chaqiriladi. Token mavjud bo'lmasa xatolik tashlamaydi.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        startBot();
    }

    /**
     * Botni (qayta) ishga tushiradi:
     * - avval oldingi sessiya bo'lsa to'xtatadi
     * - bazadan faol tokenni o'qishga urinadi; topilmasa jim qaytadi
     * - token bo'lsa, yangi {@link CrmTelegramBot} yaratib Telegram bilan ulaydi
     * <p>
     * Bu metod hech qachon exception otmaydi — barcha xatoliklar loglanadi va
     * {@code botConnected} holati mos ravishda yangilanadi.
     */
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
                    sessionService
            );

            if (telegramBotsApi == null) {
                telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            }

            activeBotSession = telegramBotsApi.registerBot(bot);
            botSettingsService.markConnected(true);
            log.info("Telegram bot muvaffaqiyatli ulandi: {}", settings.getBotUsername());
        } catch (Exception e) {
            botSettingsService.markConnected(false);
            log.error("Telegram botni ulashda xatolik. Token yoki tarmoq sozlamalarini tekshiring.", e);
        }
    }

    /** Bot ulanmagan/o'chirilgan bo'lishi kerak bo'lganda (masalan admin token o'chirsa) chaqiriladi. */
    public synchronized void stopBot() {
        stopBotIfRunning();
        botSettingsService.markConnected(false);
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