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
import uz.script.wincrm.payment.Payment;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.telegram.TelegramUser;
import uz.script.wincrm.telegram.TelegramUserRole;
import uz.script.wincrm.telegram.keyboard.TelegramKeyboards;
import uz.script.wincrm.telegram.repository.TelegramUserRepository;
import uz.script.wincrm.telegram.session.BotConversationState;
import uz.script.wincrm.telegram.session.BotSessionService;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * WinCRM Telegram bot.
 * <p>
 * MUHIM: bu klass ataylab {@code @Component} EMAS. Agar Spring uni avtomatik
 * bean sifatida yaratsa, konstruktor ilova ko'tarilishi paytida token
 * so'raydi va agar admin hali token kiritmagan bo'lsa butun ilova
 * ishga tushmay qoladi. Shu sababli bu obyekt faqat
 * {@link uz.script.wincrm.telegram.config.TelegramBotLifecycleService}
 * tomonidan, token bazada mavjud bo'lgandagina, qo'lda ({@code new}) yaratiladi.
 * <p>
 * Funksiyalar: ro'yxatdan o'tish (telefon raqami orqali), buyurtmalar,
 * to'lovlar, umumiy qarz va buyurtma bo'yicha akt sverka.
 */
@Slf4j
public class CrmTelegramBot extends TelegramLongPollingBot {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final String botUsername;
    private final TelegramUserRepository telegramUserRepository;
    private final ClientRepository clientRepository;
    private final TelegramKeyboards keyboards;
    private final BotSessionService sessionService;

    public CrmTelegramBot(
            String token,
            String botUsername,
            TelegramUserRepository telegramUserRepository,
            ClientRepository clientRepository,
            TelegramKeyboards keyboards,
            BotSessionService sessionService
    ) {
        super(token);
        this.botUsername = botUsername;
        this.telegramUserRepository = telegramUserRepository;
        this.clientRepository = clientRepository;
        this.keyboards = keyboards;
        this.sessionService = sessionService;
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

        // Client bazasida shu telefon raqami bo'yicha mos yozuvni topib bog'laymiz.
        // NOTE: ClientRepository'ga findByPhone(String) qo'shilgach shu metoddan foydalaning
        // (hozircha faqat existsByPhone bor edi) — QOLLANMA.md'dagi 6-bo'limga qarang.
        clientRepository.findAll().stream()
                .filter(c -> phone.equals(normalizePhone(c.getPhone())))
                .findFirst()
                .ifPresent(user::setClient);

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
        if (client == null || client.getSaleOrders() == null || client.getSaleOrders().isEmpty()) {
            sendPlain(user.getChatId(), "Sizda hozircha buyurtmalar mavjud emas.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83D\uDCE6 Sizning buyurtmalaringiz:\n\n");
        client.getSaleOrders().forEach(order ->
                sb.append("\u2022 №").append(order.getId())
                        .append(" | ").append(order.getOrderDate() != null ? order.getOrderDate().format(DATE_FORMAT) : "-")
                        .append(" | Jami: ").append(formatSum(order.getTotalSum()))
                        .append(" | To'langan: ").append(formatSum(order.getPaidSum()))
                        .append(" | Qarz: ").append(formatSum(order.getDebtSum()))
                        .append(" | Holat: ").append(order.getSalesOrderStatus())
                        .append("\n")
        );

        sendPlain(user.getChatId(), sb.toString());
    }

    private void sendPayments(TelegramUser user) {
        Client client = user.getClient();
        if (client == null || client.getPayments() == null || client.getPayments().isEmpty()) {
            sendPlain(user.getChatId(), "Sizda hozircha to'lovlar mavjud emas.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83D\uDCB3 Sizning to'lovlaringiz:\n\n");
        client.getPayments().stream()
                .sorted((p1, p2) -> {
                    if (p1.getPaymentDate() == null || p2.getPaymentDate() == null) return 0;
                    return p2.getPaymentDate().compareTo(p1.getPaymentDate());
                })
                .forEach(payment -> {
                    String orderInfo = payment.getSaleOrder() != null
                            ? " (Buyurtma №" + payment.getSaleOrder().getId() + ")"
                            : "";
                    sb.append("\u2022 ").append(payment.getPaymentDate() != null ? payment.getPaymentDate().format(DATE_FORMAT) : "-")
                            .append(" | ").append(formatSum(payment.getPaymentAmount()))
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

        List<SaleOrder> orders = client.getSaleOrders() == null ? List.of() : client.getSaleOrders();

        // SaleOrder'da tayyor totalSum / paidSum / debtSum bor — shulardan foydalanamiz.
        BigDecimal totalOrders = orders.stream()
                .map(o -> nvl(o.getTotalSum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = orders.stream()
                .map(o -> nvl(o.getPaidSum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebt = orders.stream()
                .map(o -> nvl(o.getDebtSum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String text = "\uD83D\uDCB0 Umumiy hisobot:\n\n" +
                "Jami buyurtmalar summasi: " + formatSum(totalOrders) + "\n" +
                "Jami to'langan summa: " + formatSum(totalPaid) + "\n" +
                "Umumiy qarzdorlik: " + formatSum(totalDebt);

        sendPlain(user.getChatId(), text);
    }

    private void sendAktOrderSelection(TelegramUser user) {
        Client client = user.getClient();
        if (client == null || client.getSaleOrders() == null || client.getSaleOrders().isEmpty()) {
            sendPlain(user.getChatId(), "Akt sverka olish uchun buyurtmalar mavjud emas.");
            return;
        }

        List<TelegramKeyboards.OrderButton> buttons = client.getSaleOrders().stream()
                .map(o -> new TelegramKeyboards.OrderButton(
                        o.getId(),
                        "№" + o.getId() + " (" + (o.getOrderDate() != null ? o.getOrderDate().format(DATE_FORMAT) : "-") + ")"
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

        Client client = userOpt.get().getClient();

        SaleOrder order = client.getSaleOrders().stream()
                .filter(o -> orderId.equals(o.getId()))
                .findFirst()
                .orElse(null);

        if (order == null) {
            sendPlain(chatId, "Buyurtma topilmadi yoki sizga tegishli emas.");
            return;
        }

        List<Payment> payments = order.getPayments();
        if (payments == null || payments.isEmpty()) {
            sendPlain(chatId, "Bu buyurtma bo'yicha to'lovlar topilmadi.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83E\uDDFE Akt sverka — Buyurtma №" + orderId + "\n\n" +
                "Buyurtma summasi: " + formatSum(order.getTotalSum()) + "\n\n" +
                "To'langan summalar ro'yxati:\n");

        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) {
            sb.append("\u2022 ").append(payment.getPaymentDate() != null ? payment.getPaymentDate().format(DATE_FORMAT) : "-")
                    .append(" | ").append(formatSum(payment.getPaymentAmount()));
            if (payment.getPaymentType() != null) {
                sb.append(" | ").append(payment.getPaymentType());
            }
            if (payment.getComment() != null && !payment.getComment().isBlank()) {
                sb.append(" | ").append(payment.getComment());
            }
            sb.append("\n");
            total = total.add(nvl(payment.getPaymentAmount()));
        }

        sb.append("\nJami to'langan: ").append(formatSum(total))
                .append("\nQoldiq qarz: ").append(formatSum(order.getDebtSum()));

        sendPlain(chatId, sb.toString());
        sessionService.setState(chatId, BotConversationState.MAIN_MENU);
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

    private void executeQuietly(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram xabar yuborishda xatolik", e);
        }
    }
}