package uz.script.wincrm.clients;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.clients.enums.NoteType;
import uz.script.wincrm.clients.enums.ReminderStatus;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = TableName.CLIENT_NOTES)
@SQLRestriction("status <> 'DELETED'")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // SaleOrder bilan to'g'ridan-to'g'ri relation o'rniga plain id saqlanadi,
    // shunda modul boshqa modullarga (sale) qattiq bog'lanmaydi
    @Column(name = "sale_order_id")
    private Long saleOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NoteType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "interaction_date")
    private LocalDateTime interactionDate;

    @Column(name = "reminder_date")
    private LocalDate reminderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_status", length = 20)
    private ReminderStatus reminderStatus;

    @Column(name = "promised_amount", precision = 18, scale = 2)
    private BigDecimal promisedAmount;
}