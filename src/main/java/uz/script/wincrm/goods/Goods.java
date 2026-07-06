package uz.script.wincrm.goods;

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

@Entity
@SQLRestriction("status <> 'DELETED'")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.GOODS)
@Getter
@Setter
@SuperBuilder
public class Goods extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_group_id", nullable = false)
    private GoodsGroup goodsGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    @Column(nullable = false)
    private BigDecimal priceCost;

    @Column(nullable = false)
    private BigDecimal priceSelling;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String photo;

}
