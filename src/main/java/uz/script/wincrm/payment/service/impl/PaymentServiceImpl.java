package uz.script.wincrm.payment.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.clients.ClientRepository;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.payment.Payment;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.payment.dto.PaymentDTO;
import uz.script.wincrm.payment.mapper.PaymentMapper;
import uz.script.wincrm.payment.repository.PaymentRepository;
import uz.script.wincrm.payment.repository.PaymentTypeRepository;
import uz.script.wincrm.payment.response.PaymentResponse;
import uz.script.wincrm.payment.service.PaymentService;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final PaymentTypeRepository paymentTypeRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final ClientRepository clientRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Payment"
    )
    public PaymentResponse create(PaymentDTO dto) {
        log.info("Create payment for client {}", dto.getClientId());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));

        PaymentType paymentType = paymentTypeRepository.findById(dto.getPaymentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + dto.getPaymentTypeId()));

        SaleOrder saleOrder = null;
        if (dto.getSaleOrderId() != null) {
            saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + dto.getSaleOrderId()));

            BigDecimal currentPaidSum = saleOrder.getPaidSum() != null ? saleOrder.getPaidSum() : BigDecimal.ZERO;
            BigDecimal remainingDebt = saleOrder.getTotalSum().subtract(currentPaidSum);

            if (dto.getPaymentAmount().compareTo(remainingDebt) > 0) {
                throw new BadRequestException(
                        "Payment amount exceeds the remaining debt of the sale order. Remaining debt: " + remainingDebt);
            }
        }

        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        Payment entity = mapper.toEntity(dto);
        entity.setClient(client);
        entity.setSaleOrder(saleOrder);
        entity.setPaymentType(paymentType);
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedUsername(username);

        entity = repository.save(entity);

        if (saleOrder != null) {
            recalculateSaleOrderSums(saleOrder);
        }

        return mapper.toResponse(entity);
    }

    @Override
    public PaymentResponse findById(Long id) {
        log.info("Fetch payment by id {}", id);

        Payment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<PaymentResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all payments");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<PaymentResponse> fetchByClientId(Long clientId, Pageable pageable) {
        log.info("Fetch payments by client id {}", clientId);

        return repository.findByClientId(clientId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<PaymentResponse> fetchBySaleOrderId(Long saleOrderId, Pageable pageable) {
        log.info("Fetch payments by sale order id {}", saleOrderId);

        return repository.findBySaleOrderId(saleOrderId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<PaymentResponse> fetchByPaymentTypeId(Long paymentTypeId, Pageable pageable) {
        log.info("Fetch payments by payment type id {}", paymentTypeId);

        return repository.findByPaymentTypeId(paymentTypeId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Payment"
    )
    public PaymentResponse update(Long id, PaymentDTO dto) {
        log.info("Update payment with id {}", id);

        Payment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        SaleOrder oldSaleOrder = entity.getSaleOrder();
        SaleOrder saleOrder = oldSaleOrder;

        if (dto.getClientId() != null
                && (entity.getClient() == null || !dto.getClientId().equals(entity.getClient().getId()))) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));
            entity.setClient(client);
        }

        if (dto.getSaleOrderId() != null
                && (oldSaleOrder == null || !dto.getSaleOrderId().equals(oldSaleOrder.getId()))) {
            saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + dto.getSaleOrderId()));
            entity.setSaleOrder(saleOrder);
        }

        if (dto.getPaymentTypeId() != null
                && (entity.getPaymentType() == null || !dto.getPaymentTypeId().equals(entity.getPaymentType().getId()))) {
            PaymentType paymentType = paymentTypeRepository.findById(dto.getPaymentTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + dto.getPaymentTypeId()));
            entity.setPaymentType(paymentType);
        }

        if (dto.getPaymentAmount() != null && saleOrder != null) {
            Payment finalEntity = entity;
            BigDecimal otherPaymentsSum = repository.findBySaleOrderId(saleOrder.getId()).stream()
                    .filter(p -> !p.getId().equals(finalEntity.getId()))
                    .map(Payment::getPaymentAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remainingDebt = saleOrder.getTotalSum().subtract(otherPaymentsSum);

            if (dto.getPaymentAmount().compareTo(remainingDebt) > 0) {
                throw new BadRequestException(
                        "Payment amount exceeds the remaining debt of the sale order. Remaining debt: " + remainingDebt);
            }
        }

        mapper.updateEntity(entity, dto);
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        entity.setCreatedUsername(username);

        entity = repository.save(entity);

        if (saleOrder != null) {
            recalculateSaleOrderSums(saleOrder);
        }
        if (oldSaleOrder != null && !Objects.equals(oldSaleOrder.getId(), saleOrder != null ? saleOrder.getId() : null)) {
            recalculateSaleOrderSums(oldSaleOrder);
        }

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Payment"
    )
    public void delete(Long id) {
        log.info("Delete payment with id {}", id);

        Payment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        SaleOrder saleOrder = entity.getSaleOrder();

        entity.setStatus(Status.DELETED);
        repository.save(entity);

        if (saleOrder != null) {
            recalculateSaleOrderSums(saleOrder);
        }
    }

    /**
     * SaleOrder ning paidSum va debtSum qiymatlarini shu order bo'yicha
     * mavjud (DELETED bo'lmagan) barcha to'lovlar asosida qayta hisoblaydi.
     * SaleOrder'ga bog'lanmagan (umumiy/avans) to'lovlar bu hisobga kirmaydi.
     */
    private void recalculateSaleOrderSums(SaleOrder saleOrder) {
        List<Payment> payments = repository.findBySaleOrderId(saleOrder.getId());

        BigDecimal paidSum = payments.stream()
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal debtSum = saleOrder.getTotalSum().subtract(paidSum);

        saleOrder.setPaidSum(paidSum);
        saleOrder.setDebtSum(debtSum);

        saleOrderRepository.save(saleOrder);
    }
}