package uz.script.wincrm.suppliers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.payment.repository.PaymentTypeRepository;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.SupplierPayment;
import uz.script.wincrm.suppliers.dto.SupplierPaymentDTO;
import uz.script.wincrm.suppliers.dto.SupplierPaymentFilterDTO;
import uz.script.wincrm.suppliers.mapper.SupplierPaymentMapper;
import uz.script.wincrm.suppliers.repository.SupplierPaymentRepository;
import uz.script.wincrm.suppliers.repository.SupplierRepository;
import uz.script.wincrm.suppliers.response.SupplierPaymentResponse;
import uz.script.wincrm.suppliers.service.SupplierBalanceService;
import uz.script.wincrm.suppliers.service.SupplierPaymentService;
import uz.script.wincrm.suppliers.specification.SupplierPaymentSpecification;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierPaymentServiceImpl implements SupplierPaymentService {

    private final SupplierPaymentRepository repository;
    private final SupplierRepository supplierRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final SupplierBalanceService supplierBalanceService;

    @Override
    @Transactional
    @Auditable(action = AuditAction.CREATE, entity = "SupplierPayment")
    public SupplierPaymentResponse create(SupplierPaymentDTO dto) {

        log.info("Creating supplier payment. SupplierId: {}, Sum: {}",
                dto.getSupplierId(), dto.getPaidSumm());

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with id: " + dto.getSupplierId()));

        PaymentType paymentType = paymentTypeRepository.findById(dto.getPaymentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment type not found with id: " + dto.getPaymentTypeId()));

        SupplierPayment payment = SupplierPaymentMapper.toEntity(dto, supplier, paymentType);
        payment.setStatus(Status.ACTIVE);

        payment = repository.save(payment);

        supplierBalanceService.increasePayment(supplier.getId(), dto.getPaidSumm());

        log.info("Supplier payment created successfully. ID: {}", payment.getId());

        return SupplierPaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.UPDATE, entity = "SupplierPayment")
    public SupplierPaymentResponse update(Long id, SupplierPaymentDTO dto) {

        log.info("Updating supplier payment with id: {}", id);

        SupplierPayment payment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier payment not found with id: " + id));

        PaymentType paymentType = paymentTypeRepository.findById(dto.getPaymentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment type not found with id: " + dto.getPaymentTypeId()));

        // Eslatma: to'lovni boshqa supplierga o'tkazish qo'llab-quvvatlanmaydi,
        // faqat summa/sana/izoh/turi o'zgartiriladi.
        BigDecimal oldSumm = payment.getPaidSumm();
        BigDecimal newSumm = dto.getPaidSumm();

        SupplierPaymentMapper.updateEntity(payment, dto, paymentType);

        payment = repository.save(payment);

        // Balansni farq asosida to'g'rilash
        BigDecimal diff = newSumm.subtract(oldSumm);
        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            supplierBalanceService.increasePayment(payment.getSupplier().getId(), diff);
        } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
            supplierBalanceService.decreasePayment(payment.getSupplier().getId(), diff.abs());
        }

        log.info("Supplier payment updated successfully. ID: {}", payment.getId());

        return SupplierPaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.DELETE, entity = "SupplierPayment")
    public void delete(Long id) {

        log.info("Deleting supplier payment with id: {}", id);

        SupplierPayment payment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier payment not found with id: " + id));

        payment.setStatus(Status.DELETED);

        repository.save(payment);

        supplierBalanceService.decreasePayment(payment.getSupplier().getId(), payment.getPaidSumm());

        log.info("Supplier payment deleted successfully. ID: {}", id);
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.READ, entity = "SupplierPayment")
    public SupplierPaymentResponse findById(Long id) {

        log.info("Fetching supplier payment with id: {}", id);

        SupplierPayment payment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier payment not found with id: " + id));

        return SupplierPaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.READ, entity = "SupplierPayment")
    public Page<SupplierPaymentResponse> findAll(Pageable pageable) {

        log.info("Fetching all supplier payments. Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<SupplierPayment> payments = repository.findAll(pageable);

        log.info("Fetched {} supplier payments.", payments.getTotalElements());

        return payments.map(SupplierPaymentMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(action = AuditAction.READ, entity = "SupplierPayment")
    public Page<SupplierPaymentResponse> filter(SupplierPaymentFilterDTO filter, Pageable pageable) {

        log.info("Filtering supplier payments. Filter: {}, Page: {}, Size: {}",
                filter, pageable.getPageNumber(), pageable.getPageSize());

        Page<SupplierPayment> payments = repository.findAll(
                SupplierPaymentSpecification.filter(filter),
                pageable
        );

        log.info("Filter completed. Total payments found: {}", payments.getTotalElements());

        return payments.map(SupplierPaymentMapper::toResponse);
    }
}