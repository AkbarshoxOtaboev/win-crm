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
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.payment.dto.PaymentTypeDTO;
import uz.script.wincrm.payment.mapper.PaymentTypeMapper;
import uz.script.wincrm.payment.repository.PaymentTypeRepository;
import uz.script.wincrm.payment.response.PaymentTypeResponse;
import uz.script.wincrm.payment.service.PaymentTypeService;
import uz.script.wincrm.utils.Status;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository repository;
    private final PaymentTypeMapper mapper;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "PaymentType"
    )
    public PaymentTypeResponse create(PaymentTypeDTO dto) {
        log.info("Create payment type {}", dto.getName());

        repository.findByNameIgnoreCase(dto.getName())
                .ifPresent(existing -> {
                    throw new BadRequestException("Payment type already exists with name: " + dto.getName());
                });


        PaymentType entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public PaymentTypeResponse findById(Long id) {
        log.info("Fetch payment type by id {}", id);

        PaymentType entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<PaymentTypeResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all payment types");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "PaymentType"
    )
    public PaymentTypeResponse update(Long id, PaymentTypeDTO dto) {
        log.info("Update payment type with id {}", id);

        PaymentType entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + id));

        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(entity.getName())) {
            repository.findByNameIgnoreCase(dto.getName())
                    .ifPresent(existing -> {
                        throw new BadRequestException("Payment type already exists with name: " + dto.getName());
                    });
        }

        mapper.updateEntity(entity, dto);
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        entity.setCreatedUsername(username);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "PaymentType"
    )
    public void delete(Long id) {
        log.info("Delete payment type with id {}", id);

        PaymentType entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }
}