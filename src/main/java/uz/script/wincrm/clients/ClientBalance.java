package uz.script.wincrm.clients;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLRestriction("status <> 'DELETED' ")
@Table(name = TableName.CLIENT_BALANCES)
public class ClientBalance extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", unique = true)
    private Client client;

    @Column(nullable = false)
    private BigDecimal totalPurchase = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalPaid = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalDebt = BigDecimal.ZERO;

    private LocalDateTime lastUpdated;
}
