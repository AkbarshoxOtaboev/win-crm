package uz.script.wincrm.telegram.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Eskiz integratsiyasida ishlatilgan AES/GCM shifrlash yondashuvi bilan bir xil:
 * kalit environment variable orqali beriladi (ESKIZ_ENCRYPTION_SECRET bilan
 * bir xil o'zgaruvchidan foydalanish ham mumkin — shuning uchun property nomi
 * konfiguratsiya qilinadigan qilib ochilgan).
 *
 * NOTE: Agar loyihada allaqachon umumiy (shared) shifrlash komponenti mavjud
 * bo'lsa (masalan EskizCredentialEncryptor kabi), o'sha komponentdan
 * foydalanish tavsiya etiladi va bu klass olib tashlanishi mumkin — kod
 * takrorlanishining oldini olish uchun.
 */
@Slf4j
@Component
public class BotTokenEncryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    private final SecretKey secretKey;

    public BotTokenEncryptor(
            @Value("${telegram.bot.encryption-secret:${TELEGRAM_BOT_ENCRYPTION_SECRET:}}") String rawSecret
    ) {
        if (rawSecret == null || rawSecret.isBlank()) {
            log.warn("TELEGRAM_BOT_ENCRYPTION_SECRET topilmadi! Vaqtinchalik default kalit " +
                    "ishlatilyapti — PRODUCTION uchun albatta environment variable qo'ying.");
            rawSecret = "temporary-default-secret-change-me-32b!!";
        }
        byte[] keyBytes = normalizeKey(rawSecret);
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    /** 32 baytga (AES-256) moslashtirish uchun oddiy padding/truncate. */
    private byte[] normalizeKey(String secret) {
        byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[32];
        System.arraycopy(raw, 0, result, 0, Math.min(raw.length, 32));
        return result;
    }

    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("Token shifrlashda xatolik", e);
            throw new IllegalStateException("Token shifrlab bo'lmadi", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);

            byte[] cipherText = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, iv.length, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Token deshifrlashda xatolik", e);
            throw new IllegalStateException("Token deshifrlab bo'lmadi", e);
        }
    }
}