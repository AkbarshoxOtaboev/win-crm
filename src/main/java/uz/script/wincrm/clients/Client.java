package uz.script.wincrm.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.CLIENTS)
@SQLRestriction("status <> 'DELETED' ")
@SuperBuilder
public class Client extends BaseEntity {
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;
}
