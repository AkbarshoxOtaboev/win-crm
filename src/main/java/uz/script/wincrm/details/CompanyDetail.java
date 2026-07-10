package uz.script.wincrm.details;

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
@SuperBuilder
@Table(name = TableName.COMPANY_DETAIL)
@SQLRestriction("status <> 'DELETED'")
public class CompanyDetail extends BaseEntity {
    private String companyName;      // Название// Краткое название
    private String inn;              // ИНН
    private String oked;             // ОКЭД
    private String mfo;              // МФО
    private String accountNumber;    // Расчетный счет
    private String bankName;         // Банк
    private String director;         // Директор
    private String phone;            // Телефон
    private String email;            // Email
    private String address;          // Адрес// Логотип
    private String description;      // Примечание
}
