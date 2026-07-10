package uz.script.wincrm.details.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.details.CompanyDetail;
import uz.script.wincrm.details.dto.CompanyDetailDTO;
import uz.script.wincrm.details.mapper.CompanyDetailMapper;
import uz.script.wincrm.details.repository.CompanyDetailRepository;
import uz.script.wincrm.details.response.CompanyDetailResponse;
import uz.script.wincrm.details.service.CompanyDetailService;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompanyDetailServiceImpl implements CompanyDetailService {

    private final CompanyDetailRepository repository;
    private final CompanyDetailMapper mapper;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "CompanyDetail"
    )
    public CompanyDetailResponse create(CompanyDetailDTO dto) {
        log.info("Create company detail {}", dto.getCompanyName());

        repository.findByInn(dto.getInn())
                .ifPresent(existing -> {
                    throw new AlreadyExistsException("Company detail already exists with INN: " + dto.getInn());
                });

        CompanyDetail entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public CompanyDetailResponse findById(Long id) {
        log.info("Fetch company detail by id {}", id);

        CompanyDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company detail not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public CompanyDetailResponse getCompanyDetail() {
        log.info("Fetch the company detail");

        CompanyDetail entity = repository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Company detail has not been configured yet"));

        return mapper.toResponse(entity);
    }

    @Override
    public List<CompanyDetailResponse> fetchAll() {
        log.info("Fetch all company details");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "CompanyDetail"
    )
    public CompanyDetailResponse update(Long id, CompanyDetailDTO dto) {
        log.info("Update company detail with id {}", id);

        CompanyDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company detail not found with id: " + id));

        if (dto.getInn() != null && !dto.getInn().equals(entity.getInn())) {
            repository.findByInn(dto.getInn())
                    .ifPresent(existing -> {
                        throw new AlreadyExistsException("Company detail already exists with INN: " + dto.getInn());
                    });
        }

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "CompanyDetail"
    )
    public void delete(Long id) {
        log.info("Delete company detail with id {}", id);

        CompanyDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company detail not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }
}