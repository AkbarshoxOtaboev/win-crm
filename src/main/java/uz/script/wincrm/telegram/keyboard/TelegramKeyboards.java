package uz.script.wincrm.telegram.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramKeyboards {

    public static final String BTN_ORDERS = "\uD83D\uDCE6 Buyurtmalarim";
    public static final String BTN_PAYMENTS = "\uD83D\uDCB3 To'lovlarim";
    public static final String BTN_DEBT = "\uD83D\uDCB0 Umumiy qarz";
    public static final String BTN_AKT = "\uD83E\uDDFE Akt sverka";
    public static final String BTN_BACK = "\u2B05\uFE0F Bosh menyu";

    /** /start bosganda telefon raqamini so'raydigan tugma. */
    public ReplyKeyboardMarkup requestContactKeyboard() {
        KeyboardButton contactButton = new KeyboardButton("\uD83D\uDCDE Telefon raqamni yuborish");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(List.of(row));
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    /** Ro'yxatdan o'tgandan keyingi asosiy menyu. */
    public ReplyKeyboardMarkup mainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(BTN_ORDERS));
        row1.add(new KeyboardButton(BTN_PAYMENTS));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(BTN_DEBT));
        row2.add(new KeyboardButton(BTN_AKT));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(List.of(row1, row2));
        markup.setResizeKeyboard(true);
        return markup;
    }

    /** Akt sverka uchun buyurtmalar ro'yxatini inline tugmalar sifatida ko'rsatish. */
    public InlineKeyboardMarkup ordersInlineKeyboard(List<OrderButton> orders) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (OrderButton order : orders) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(order.label());
            button.setCallbackData("AKT_ORDER_" + order.id());
            rows.add(List.of(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public record OrderButton(Long id, String label) {
    }
}