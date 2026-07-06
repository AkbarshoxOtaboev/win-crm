package uz.script.wincrm.goods;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.util.List;

@Entity
@SQLRestriction("status <> 'DELETED'")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.GOODS_GROUP)
@Getter
@Setter
public class GoodsGroup extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "goodsGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Goods> goodsList;

}
