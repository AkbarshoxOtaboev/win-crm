package uz.script.wincrm.suppliers;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.WarehouseOrder;

import java.util.List;

@Entity
@Table(name = TableName.SUPPLIERS)
@SQLRestriction("status <> 'DELETED' ")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {

    @Column(nullable = false)
    private String name;

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

    @OneToMany(mappedBy = "supplier")
    private List<WarehouseOrder> warehouseOrders;

    @OneToOne(
            mappedBy = "supplier",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private SupplierBalance balance;
}