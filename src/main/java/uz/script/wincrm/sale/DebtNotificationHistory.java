package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.sale.enums.DebtNotificationStatus;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Qarz haqida mijozga yuborilgan har bir SMS'ning tarixiy yozuvi.
 * Muvaffaqiyatli va muvaffaqiyatsiz urinishlar ham saqlanadi (status maydoni orqali).
 *
 * DIQQAT: uz.script.wincrm.utils.TableName klassiga
 * "DEBT_NOTIFICATION_HISTORY" konstantasini qo'shishni unutmang.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.DEBT_NOTIFICATION_HISTORY)
public class DebtNotificationHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private BigDecimal totalDebtAmount;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtNotificationStatus debtNotificationStatus;

    @Column(length = 500)
    private String errorMessage;
}