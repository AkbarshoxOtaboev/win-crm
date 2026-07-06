package uz.script.wincrm.suppliers.response;

import lombok.Builder;
import lombok.Data;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class SupplierResponse {

    private Long id;

    private String name;

    private String inn;

    private String phone;

    private String additionalPhone;

    private String address;

    private String bankName;

    private String mfo;

    private String accountNumber;

    private String description;

    private Status status;

    private String createdUsername;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}