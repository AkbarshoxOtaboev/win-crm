package uz.script.wincrm.sms;

/**
 * SMS yuborishda (Eskiz.uz bilan bog'lanishda) yuzaga keladigan xatolik.
 */
public class SmsSendException extends RuntimeException {
    public SmsSendException(String message) {
        super(message);
    }

    public SmsSendException(String message, Throwable cause) {
        super(message, cause);
    }
}