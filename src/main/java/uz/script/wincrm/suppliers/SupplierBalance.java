package uz.script.wincrm.suppliers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
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
@Table(name = TableName.SUPPLIER_BALANCE)
public class SupplierBalance extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", unique = true)
    private Supplier supplier;

    @Column(nullable = false)
    private BigDecimal totalPurchase = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalPaid = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalDebt = BigDecimal.ZERO;

    private LocalDateTime lastUpdated;
}
