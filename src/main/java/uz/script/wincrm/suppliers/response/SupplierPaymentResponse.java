package uz.script.wincrm.suppliers.response;

import lombok.Builder;
import lombok.Data;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SupplierPaymentResponse {

    private Long id;

    private Long supplierId;

    private String supplierName;

    private BigDecimal paidSumm;

    private LocalDateTime paidDate;

    private String comment;

    private Long paymentTypeId;

    private String paymentTypeName;

    private Status status;

    private String createdUsername;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}