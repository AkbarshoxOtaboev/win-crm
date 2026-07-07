package uz.script.wincrm.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.dto.WarehouseDTO;
import uz.script.wincrm.warehouse.mapper.WarehouseMapper;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;
import uz.script.wincrm.warehouse.response.WarehouseResponse;
import uz.script.wincrm.warehouse.service.WarehouseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    @Override
    public boolean existsByName(String name) {
        return name != null && !name.isBlank() && repository.existsByName(name);
    }

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Warehouse"
    )
//    @CacheEvict(value = "warehouses", allEntries = true)
    public WarehouseResponse create(WarehouseDTO dto) {
        log.info("Create warehouse");

        if (existsByName(dto.getName())) {
            throw new AlreadyExistsException(
                    "Warehouse with name '" + dto.getName() + "' already exists");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Warehouse warehouse = mapper.toEntity(dto);
        warehouse.setCreatedUsername(username);

        warehouse = repository.save(warehouse);

        return mapper.toResponse(warehouse);
    }

    @Override
//    @Cacheable(value = "warehouse", key = "#id")
    public WarehouseResponse findById(Long id) {
        log.info("Fetch warehouse by id {}", id);

        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse not found with id: " + id));

        return mapper.toResponse(warehouse);
    }

    @Override
//    @Cacheable(value = "warehouses")
    public List<WarehouseResponse> fetchAllWarehouses() {
        log.info("Fetch all warehouses");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Warehouse"
    )
//    @CacheEvict(value = {"warehouses", "warehouse"}, allEntries = true)
    public WarehouseResponse update(Long id, WarehouseDTO dto) {
        log.info("Update warehouse with id {}", id);

        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse not found with id: " + id));

        if (!warehouse.getName().equals(dto.getName()) && existsByName(dto.getName())) {
            throw new AlreadyExistsException(
                    "Warehouse with name '" + dto.getName() + "' already exists");
        }

        mapper.updateEntity(warehouse, dto);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        warehouse.setCreatedUsername(username);
        warehouse = repository.save(warehouse);

        return mapper.toResponse(warehouse);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Warehouse"
    )
//    @CacheEvict(value = {"warehouses", "warehouse"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete warehouse with id {}", id);

        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Warehouse not found with id: " + id));

        warehouse.setStatus(Status.DELETED);
        repository.save(warehouse);
    }
}
