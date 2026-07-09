package uz.script.wincrm.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.PAYMENT_TYPES)
@SQLRestriction("status <> 'DELETED'")
public class PaymentType extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "paymentType")
    private List<Payment> payments;
}
