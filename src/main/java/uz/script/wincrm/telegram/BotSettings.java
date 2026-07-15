package uz.script.wincrm.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

/**
 * Bitta CRM instansiyasi uchun bitta Telegram bot sozlamasi (singleton row).
 * Token BotFather'dan olinadi va bazada AES/GCM bilan shifrlangan holda saqlanadi
 * (Eskiz integratsiyasida ishlatilgan pattern bilan bir xil yondashuv).
 */
@Entity
@Table(name = TableName.BOT_SETTINGS)
@SQLRestriction("status <> 'DELETED'")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BotSettings extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String botUsername; // masalan: @WinCrmBot

    /**
     * BotFather bergan token AES/GCM bilan shifrlanib saqlanadi.
     * Ochiq (plain) token hech qachon bazaga yozilmaydi.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedToken;

    /** Admin panelda ko'rsatish uchun tokenning oxirgi belgilari, masalan "****ueVa". */
    @Column(length = 20)
    private String tokenHint;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;


    /**
     * Bot muvaffaqiyatli registratsiya (long-polling'ga ulangan) bo'lganini bildiradi.
     * Runtime holatidir, lekin kuzatish uchun bazada ham saqlanadi.
     */
    @Column
    private boolean botConnected;
}