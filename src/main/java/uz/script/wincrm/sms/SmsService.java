package uz.script.wincrm.sms;

public interface SmsService {

    /**
     * Berilgan telefon raqamiga SMS yuboradi.
     *
     * @param phoneNumber telefon raqami (998901234567 yoki 901234567 formatida)
     * @param message     SMS matni
     */
    void sendSms(String phoneNumber, String message);
}