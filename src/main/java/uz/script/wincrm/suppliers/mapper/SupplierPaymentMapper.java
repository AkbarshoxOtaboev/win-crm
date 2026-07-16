package uz.script.wincrm.suppliers.mapper;

import lombok.experimental.UtilityClass;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.SupplierPayment;
import uz.script.wincrm.suppliers.dto.SupplierPaymentDTO;
import uz.script.wincrm.suppliers.response.SupplierPaymentResponse;

@UtilityClass
public class SupplierPaymentMapper {

    public SupplierPayment toEntity(SupplierPaymentDTO dto, Supplier supplier, PaymentType paymentType) {
        if (dto == null) {
            return null;
        }

        return SupplierPayment.builder()
                .supplier(supplier)
                .paidSumm(dto.getPaidSumm())
                .paidDate(dto.getPaidDate())
                .comment(dto.getComment())
                .paymentType(paymentType)
                .build();
    }

    public SupplierPaymentResponse toResponse(SupplierPayment payment) {
        if (payment == null) {
            return null;
        }

        return SupplierPaymentResponse.builder()
                .id(payment.getId())
                .supplierId(payment.getSupplier() != null ? payment.getSupplier().getId() : null)
                .supplierName(payment.getSupplier() != null ? payment.getSupplier().getName() : null)
                .paidSumm(payment.getPaidSumm())
                .paidDate(payment.getPaidDate())
                .comment(payment.getComment())
                .paymentTypeId(payment.getPaymentType() != null ? payment.getPaymentType().getId() : null)
                .paymentTypeName(payment.getPaymentType() != null ? payment.getPaymentType().getName() : null)
                .status(payment.getStatus())
                .createdUsername(payment.getCreatedUsername())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public void updateEntity(SupplierPayment payment, SupplierPaymentDTO dto, PaymentType paymentType) {
        payment.setPaidSumm(dto.getPaidSumm());
        payment.setPaidDate(dto.getPaidDate());
        payment.setComment(dto.getComment());
        payment.setPaymentType(paymentType);
    }
}