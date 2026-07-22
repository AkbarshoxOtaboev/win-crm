package uz.script.wincrm.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.clients.repository.ClientRepository;
import uz.script.wincrm.telegram.TelegramUser;
import uz.script.wincrm.telegram.TelegramUserRole;
import uz.script.wincrm.telegram.keyboard.TelegramKeyboards;
import uz.script.wincrm.telegram.repository.TelegramUserRepository;
import uz.script.wincrm.telegram.service.TelegramBotDataService;
import uz.script.wincrm.telegram.session.BotConversationState;
import uz.script.wincrm.telegram.session.BotSessionService;
import uz.script.wincrm.telegram.view.PaymentView;
import uz.script.wincrm.telegram.view.SaleOrderView;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * WinCRM Telegram bot.
 * <p>
 * MUHIM: bu klass ataylab {@code @Component} EMAS — {@link
 * uz.script.wincrm.telegram.config.TelegramBotLifecycleService} tomonidan
 * token bazada mavjud bo'lgandagina qo'lda ({@code new}) yaratiladi.
 * <p>
 * MUHIM #2: barcha buyurtma/to'lov/balans ma'lumotlari {@link TelegramBotDataService}
 * orqali (JPQL constructor-expression proyeksiyalar) olinadi — bu klass hech qachon
 * Client/SaleOrder/Payment entity'larining lazy relation'larini bevosita navigatsiya
 * qilmaydi, chunki bot Spring bean emas va @Transactional context'da ishlamaydi.
 * <p>
 * Funksiyalar: ro'yxatdan o'tish (telefon raqami orqali, shu jumladan asosiy
 * bazada mavjud mijozlarni avtomatik bog'lash), buyurtmalar, to'lovlar, umumiy
 * qarz, akt sverka va admin panelidan tanlangan mijozga xabar yuborish.
 */
@Slf4j
public class CrmTelegramBot extends TelegramLongPollingBot {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final String botUsername;
    private final TelegramUserRepository telegramUserRepository;
    private final ClientRepository clientRepository;
    private final TelegramKeyboards keyboards;
    private final BotSessionService sessionService;
    private final TelegramBotDataService botDataService;

    public CrmTelegramBot(
            String token,
            String botUsername,
            TelegramUserRepository telegramUserRepository,
            ClientRepository clientRepository,
            TelegramKeyboards keyboards,
            BotSessionService sessionService,
            TelegramBotDataService botDataService
    ) {
        super(token);
        this.botUsername = botUsername;
        this.telegramUserRepository = telegramUserRepository;
        this.clientRepository = clientRepository;
        this.keyboards = keyboards;
        this.sessionService = sessionService;
        this.botDataService = botDataService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                handleCallback(update.getCallbackQuery());
                return;
            }

            if (!update.hasMessage()) {
                return;
            }

            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (message.hasContact()) {
                handleContact(chatId, message.getContact());
                return;
            }

            if (!message.hasText()) {
                return;
            }

            String text = message.getText().trim();

            if (text.equals("/start")) {
                handleStart(chatId);
                return;
            }

            Optional<TelegramUser> userOpt = telegramUserRepository.findByChatId(chatId);
            if (userOpt.isEmpty()) {
                handleStart(chatId);
                return;
            }

            TelegramUser user = userOpt.get();
            routeMainMenu(user, text);

        } catch (Exception e) {
            log.error("Telegram update'ni qayta ishlashda xatolik", e);
        }
    }

    // ---------------------------------------------------------------
    // Registratsiya
    // ---------------------------------------------------------------

    private void handleStart(Long chatId) {
        sessionService.setState(chatId, BotConversationState.AWAITING_PHONE);
        sendWithKeyboard(chatId,
                "Assalomu alaykum! WinCRM botiga xush kelibsiz.\n\n" +
                        "Buyurtmalaringiz, to'lovlaringiz va qarzdorligingiz haqida ma'lumot olish uchun " +
                        "telefon raqamingizni yuboring.",
                keyboards.requestContactKeyboard());
    }

    private void handleContact(Long chatId, Contact contact) {
        String phone = normalizePhone(contact.getPhoneNumber());

        Optional<TelegramUser> existing = telegramUserRepository.findByPhone(phone);
        TelegramUser user = existing.orElseGet(() -> TelegramUser.builder()
                .chatId(chatId)
                .phone(phone)
                .role(TelegramUserRole.CLIENT)
                .status(Status.ACTIVE)
                .verified(true)
                .build());

        user.setChatId(chatId);
        user.setFirstName(contact.getFirstName());
        user.setLastName(contact.getLastName());
        user.setTelegramUsername(contact.getUserId() != null ? String.valueOf(contact.getUserId()) : null);

        // Asosiy bazadagi (Clients jadvalidagi) mavjud mijozlar botdan ro'yxatdan
        // o'tganda avtomatik bog'lanadi — indekslangan findByPhone orqali
        // (avvalgi versiyada findAll().stream().filter(...) bilan BUTUN jadval
        // yuklanardi, bu N+1/performance muammosi edi — endi tuzatildi).
        clientRepository.findByPhone(phone).ifPresent(user::setClient);

        telegramUserRepository.save(user);
        sessionService.setState(chatId, BotConversationState.MAIN_MENU);

        String clientInfo = user.getClient() != null
                ? "Xush kelibsiz, " + user.getClient().getFullName() + "!"
                : "Ro'yxatdan o'tdingiz. Diqqat: bu raqam bo'yicha CRM tizimida mijoz topilmadi, " +
                "shuning uchun buyurtma/to'lov ma'lumotlari bo'sh ko'rinishi mumkin.";

        sendWithKeyboard(chatId, clientInfo + "\n\nQuyidagi menyudan kerakli bo'limni tanlang:",
                keyboards.mainMenuKeyboard());
    }

    // ---------------------------------------------------------------
    // Asosiy menyu
    // ---------------------------------------------------------------

    private void routeMainMenu(TelegramUser user, String text) {
        Long chatId = user.getChatId();

        switch (text) {
            case TelegramKeyboards.BTN_ORDERS -> sendOrders(user);
            case TelegramKeyboards.BTN_PAYMENTS -> sendPayments(user);
            case TelegramKeyboards.BTN_DEBT -> sendDebt(user);
            case TelegramKeyboards.BTN_AKT -> sendAktOrderSelection(user);
            case TelegramKeyboards.BTN_BACK -> sendWithKeyboard(chatId, "Bosh menyu", keyboards.mainMenuKeyboard());
            default -> sendPlain(chatId, "Iltimos, menyudagi tugmalardan birini tanlang.");
        }
    }

    private void sendOrders(TelegramUser user) {
        Client client = user.getClient();
        if (client == null) {
            sendPlain(user.getChatId(), "CRM tizimida mijoz topilmadi.");
            return;
        }

        List<SaleOrderView> orders = botDataService.findOrdersByClientId(client.getId());
        if (orders.isEmpty()) {
            sendPlain(user.getChatId(), "Sizda hozircha buyurtmalar mavjud emas.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83D\uDCE6 Sizning buyurtmalaringiz:\n\n");
        orders.forEach(order ->
                sb.append("\u2022 №").append(order.id())
                        .append(" | ").append(order.orderDate() != null ? order.orderDate().format(DATE_FORMAT) : "-")
                        .append(" | Jami: ").append(formatSum(order.totalSum()))
                        .append(" | To'langan: ").append(formatSum(order.paidSum()))
                        .append(" | Qarz: ").append(formatSum(order.debtSum()))
                        .append(" | Holat: ").append(order.salesOrderStatus())
                        .append("\n")
        );

        sendPlain(user.getChatId(), sb.toString());
    }

    private void sendPayments(TelegramUser user) {
        Client client = user.getClient();
        if (client == null) {
            sendPlain(user.getChatId(), "CRM tizimida mijoz topilmadi.");
            return;
        }

        List<PaymentView> payments = botDataService.findPaymentsByClientId(client.getId());
        if (payments.isEmpty()) {
            sendPlain(user.getChatId(), "Sizda hozircha to'lovlar mavjud emas.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83D\uDCB3 Sizning to'lovlaringiz:\n\n");
        payments.forEach(payment -> {
            String orderInfo = payment.saleOrderId() != null
                    ? " (Buyurtma №" + payment.saleOrderId() + ")"
                    : "";
            sb.append("\u2022 ").append(payment.paymentDate() != null ? payment.paymentDate().format(DATE_FORMAT) : "-")
                    .append(" | ").append(formatSum(payment.paymentAmount()))
                    .append(orderInfo)
                    .append("\n");
        });

        sendPlain(user.getChatId(), sb.toString());
    }

    private void sendDebt(TelegramUser user) {
        Client client = user.getClient();
        if (client == null) {
            sendPlain(user.getChatId(), "CRM tizimida mijoz topilmadi.");
            return;
        }

        String text = botDataService.findBalanceByClientId(client.getId())
                .map(balance -> "\uD83D\uDCB0 Umumiy hisobot:\n\n" +
                        "Jami xaridlar summasi: " + formatSum(balance.totalPurchase()) + "\n" +
                        "Jami to'langan summa: " + formatSum(balance.totalPaid()) + "\n" +
                        "Umumiy qarzdorlik: " + formatSum(balance.totalDebt()))
                .orElse("\uD83D\uDCB0 Sizning balansingiz hali hisoblanmagan.");

        sendPlain(user.getChatId(), text);
    }

    private void sendAktOrderSelection(TelegramUser user) {
        Client client = user.getClient();
        if (client == null) {
            sendPlain(user.getChatId(), "CRM tizimida mijoz topilmadi.");
            return;
        }

        List<SaleOrderView> orders = botDataService.findOrdersByClientId(client.getId());
        if (orders.isEmpty()) {
            sendPlain(user.getChatId(), "Akt sverka olish uchun buyurtmalar mavjud emas.");
            return;
        }

        List<TelegramKeyboards.OrderButton> buttons = orders.stream()
                .map(o -> new TelegramKeyboards.OrderButton(
                        o.id(),
                        "№" + o.id() + " (" + (o.orderDate() != null ? o.orderDate().format(DATE_FORMAT) : "-") + ")"
                ))
                .toList();

        sessionService.setState(user.getChatId(), BotConversationState.AWAITING_ORDER_FOR_AKT);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(user.getChatId()))
                .text("Akt sverka olish uchun buyurtmani tanlang:")
                .replyMarkup(keyboards.ordersInlineKeyboard(buttons))
                .build();

        executeQuietly(message);
    }

    // ---------------------------------------------------------------
    // Inline tugma bosilganda (akt sverka)
    // ---------------------------------------------------------------

    private void handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();

        if (data == null || !data.startsWith("AKT_ORDER_")) {
            return;
        }

        Long orderId = Long.valueOf(data.replace("AKT_ORDER_", ""));

        Optional<TelegramUser> userOpt = telegramUserRepository.findByChatId(chatId);
        if (userOpt.isEmpty() || userOpt.get().getClient() == null) {
            sendPlain(chatId, "Xatolik: foydalanuvchi topilmadi.");
            return;
        }

        Long clientId = userOpt.get().getClient().getId();

        Optional<SaleOrderView> orderOpt = botDataService.findOrderByIdAndClientId(orderId, clientId);
        if (orderOpt.isEmpty()) {
            sendPlain(chatId, "Buyurtma topilmadi yoki sizga tegishli emas.");
            return;
        }

        SaleOrderView order = orderOpt.get();
        List<PaymentView> payments = botDataService.findPaymentsByOrderId(orderId);
        if (payments.isEmpty()) {
            sendPlain(chatId, "Bu buyurtma bo'yicha to'lovlar topilmadi.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83E\uDDFE Akt sverka — Buyurtma №" + orderId + "\n\n" +
                "Buyurtma summasi: " + formatSum(order.totalSum()) + "\n\n" +
                "To'langan summalar ro'yxati:\n");

        BigDecimal total = BigDecimal.ZERO;
        for (PaymentView payment : payments) {
            sb.append("\u2022 ").append(payment.paymentDate() != null ? payment.paymentDate().format(DATE_FORMAT) : "-")
                    .append(" | ").append(formatSum(payment.paymentAmount()));
            if (payment.paymentTypeName() != null) {
                sb.append(" | ").append(payment.paymentTypeName());
            }
            if (payment.comment() != null && !payment.comment().isBlank()) {
                sb.append(" | ").append(payment.comment());
            }
            sb.append("\n");
            total = total.add(nvl(payment.paymentAmount()));
        }

        sb.append("\nJami to'langan: ").append(formatSum(total))
                .append("\nQoldiq qarz: ").append(formatSum(order.debtSum()));

        sendPlain(chatId, sb.toString());
        sessionService.setState(chatId, BotConversationState.MAIN_MENU);
    }

    // ---------------------------------------------------------------
    // Tashqaridan (admin panel) tanlangan mijozga xabar yuborish
    // ---------------------------------------------------------------

    /**
     * CRM admin panelidan tanlangan mijozga bot orqali erkin xabar yuborish uchun.
     * {@link uz.script.wincrm.telegram.config.TelegramBotLifecycleService#sendMessageToClient}
     * tomonidan chaqiriladi — chatId u yerda TelegramUser orqali oldindan aniqlanadi,
     * bu metod faqat xabarni yuboradi.
     *
     * @return true — xabar muvaffaqiyatli yuborilgan bo'lsa, false — Telegram API xatoligi bo'lsa
     */
    public boolean sendMessageToClientChat(Long chatId, String text) {
        return executeQuietly(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build());
    }

    // ---------------------------------------------------------------
    // Yordamchi metodlar
    // ---------------------------------------------------------------

    /** 998XXXXXXXXX formatiga normalizatsiya — Eskiz SMS integratsiyasidagi qoida bilan bir xil. */
    private String normalizePhone(String rawPhone) {
        if (rawPhone == null) return null;
        String digits = rawPhone.replaceAll("[^0-9]", "");
        if (digits.length() == 9) {
            digits = "998" + digits;
        }
        return digits;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String formatSum(BigDecimal value) {
        return nvl(value).toPlainString() + " so'm";
    }

    private void sendPlain(Long chatId, String text) {
        executeQuietly(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build());
    }

    private void sendWithKeyboard(Long chatId, String text, org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard keyboard) {
        executeQuietly(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .replyMarkup(keyboard)
                .build());
    }

    private boolean executeQuietly(SendMessage message) {
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            log.error("Telegram xabar yuborishda xatolik", e);
            return false;
        }
    }
}