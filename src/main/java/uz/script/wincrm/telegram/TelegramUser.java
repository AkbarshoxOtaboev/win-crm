package uz.script.wincrm.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

/**
 * Telegram bot orqali /start bosib, telefon raqamini yuborgan foydalanuvchi.
 * Telefon raqami orqali {@link Client} bilan bog'lanadi (Client.phone == TelegramUser.phone).
 */
@Entity
@Table(name = TableName.TELEGRAM_USERS)
@SQLRestriction("status <> 'DELETED'")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser extends BaseEntity {

    /**
     * Telegram tomonidan beriladigan noyob chat id (foydalanuvchiga xabar
     * yuborish uchun asosiy identifikator).
     */
    @Column(nullable = false, unique = true)
    private Long chatId;

    @Column(length = 64)
    private String telegramUsername; // @username (bo'lmasligi ham mumkin)

    @Column(length = 150)
    private String firstName;

    @Column(length = 150)
    private String lastName;

    /**
     * Foydalanuvchi Telegram orqali yuborgan telefon raqami, 998XXXXXXXXX
     * formatiga normalizatsiya qilingan (Eskiz SMS integratsiyasidagi
     * telefon normalizatsiya qoidasi bilan bir xil).
     */
    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TelegramUserRole role;

    /**
     * phone bo'yicha Clients jadvalidan topilgan mos yozuv. Agar mijoz hali
     * CRM'da ro'yxatga olinmagan bo'lsa, null bo'lishi mumkin.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(nullable = false)
    @lombok.Builder.Default
    private boolean verified = true;
}