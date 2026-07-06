package uz.script.wincrm.suppliers.service;

import org.springframework.transaction.annotation.Transactional;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.SupplierRepository;
import uz.script.wincrm.suppliers.SupplierService;
import uz.script.wincrm.suppliers.dto.SupplierDTO;
import uz.script.wincrm.suppliers.dto.SupplierFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierResponse;
import uz.script.wincrm.suppliers.specification.SupplierSpecification;
import uz.script.wincrm.utils.Status;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.annotation.CacheEvict;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.suppliers.mapper.SupplierMapper;


@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repository;

    @Override
    @Transactional
//    @CacheEvict(value = {"suppliers", "supplier"}, allEntries = true)
    @Auditable(action = AuditAction.CREATE, entity = "Supplier")
    public SupplierResponse create(SupplierDTO dto) {

        log.info("Creating supplier: {}", dto.getName());

        if (dto.getInn() != null
                && !dto.getInn().isBlank()
                && repository.existsByInn(dto.getInn())) {

            throw new AlreadyExistsException(
                    "Supplier with INN '" + dto.getInn() + "' already exists."
            );
        }

        if (repository.existsByPhone(dto.getPhone())) {

            throw new AlreadyExistsException(
                    "Supplier with phone '" + dto.getPhone() + "' already exists."
            );
        }

        Supplier supplier = SupplierMapper.toEntity(dto);
        supplier.setStatus(Status.ACTIVE);

        supplier = repository.save(supplier);

        log.info("Supplier created successfully. ID: {}", supplier.getId());

        return SupplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"suppliers", "supplier"}, allEntries = true)
    @Auditable(action = AuditAction.UPDATE, entity = "Supplier")
    public SupplierResponse update(Long id, SupplierDTO dto) {

        log.info("Updating supplier with id: {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        // INN duplicate check
        if (dto.getInn() != null
                && !dto.getInn().isBlank()
                && repository.existsByInnAndIdNot(dto.getInn(), id)) {

            throw new AlreadyExistsException(
                    "Supplier with INN '" + dto.getInn() + "' already exists."
            );
        }

        // Phone duplicate check
        if (repository.existsByPhoneAndIdNot(dto.getPhone(), id)) {

            throw new AlreadyExistsException(
                    "Supplier with phone '" + dto.getPhone() + "' already exists."
            );
        }

        SupplierMapper.updateEntity(supplier, dto);

        supplier = repository.save(supplier);

        log.info("Supplier updated successfully. ID: {}", supplier.getId());

        return SupplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"suppliers", "supplier"}, allEntries = true)
    @Auditable(action = AuditAction.DELETE, entity = "Supplier")
    public void delete(Long id) {

        log.info("Deleting supplier with id: {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setStatus(Status.DELETED);

        repository.save(supplier);

        log.info("Supplier deleted successfully. ID: {}", id);
    }

    @Override
    @Transactional
//    @Cacheable(value = "supplier", key = "#id")
    @Auditable(action = AuditAction.READ, entity = "Supplier")
    public SupplierResponse findById(Long id) {

        log.info("Fetching supplier with id: {}", id);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        log.info("Supplier fetched successfully. ID: {}", supplier.getId());

        return SupplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional
//    @Cacheable(
//            value = "suppliers",
//            key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
//    )
    @Auditable(action = AuditAction.READ, entity = "Supplier")
    public Page<SupplierResponse> findAll(Pageable pageable) {

        log.info("Fetching all suppliers. Page: {}, Size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<Supplier> suppliers = repository.findAll(pageable);

        log.info("Fetched {} suppliers.", suppliers.getTotalElements());

        return suppliers.map(SupplierMapper::toResponse);
    }

    @Override
    @Transactional
//    @Cacheable(
//            value = "suppliers-filter",
//            key = "T(java.util.Objects).hash(#filter) + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort"
//    )
    @Auditable(action = AuditAction.READ, entity = "Supplier")
    public Page<SupplierResponse> filter(SupplierFilterDTO filter, Pageable pageable) {

        log.info("Filtering suppliers. Filter: {}, Page: {}, Size: {}",
                filter,
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<Supplier> suppliers = repository.findAll(
                SupplierSpecification.filter(filter),
                pageable
        );

        log.info("Filter completed. Total suppliers found: {}",
                suppliers.getTotalElements());

        return suppliers.map(SupplierMapper::toResponse);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"suppliers", "supplier"}, allEntries = true)
    @Auditable(action = AuditAction.UPDATE, entity = "Supplier")
    public SupplierResponse changeStatus(Long id, Status status) {

        log.info("Changing supplier status. ID: {}, New Status: {}", id, status);

        Supplier supplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        if (supplier.getStatus() == status) {
            throw new BadRequestException(
                    "Supplier already has status: " + status
            );
        }

        supplier.setStatus(status);

        supplier = repository.save(supplier);

        log.info("Supplier status changed successfully. ID: {}, Status: {}",
                supplier.getId(),
                supplier.getStatus());

        return SupplierMapper.toResponse(supplier);
    }
}
