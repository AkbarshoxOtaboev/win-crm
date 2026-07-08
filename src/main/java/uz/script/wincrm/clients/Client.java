package uz.script.wincrm.clients;

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
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderItem;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.util.List;

@Entity
@Table(name = TableName.CLIENTS)
@SQLRestriction("status <> 'DELETED'")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(length = 20, unique = true)
    private String inn;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(length = 20)
    private String additionalPhone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 150)
    private String bankName;

    @Column(length = 10)
    private String mfo;

    @Column(length = 30)
    private String accountNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "client")
    private List<SaleOrder> saleOrders;

    @OneToMany(mappedBy = "client")
    private List<SaleOrderItem> saleOrderItems;
}