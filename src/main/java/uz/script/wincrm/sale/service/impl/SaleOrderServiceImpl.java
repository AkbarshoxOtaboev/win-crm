package uz.script.wincrm.sale.service.impl;

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
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.dto.SaleOrderDTO;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.sale.mapper.SaleOrderMapper;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.response.SaleOrderResponse;
import uz.script.wincrm.sale.service.SaleOrderService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository repository;
    private final SaleOrderMapper mapper;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "SaleOrder"
    )
//    @CacheEvict(value = "saleOrders", allEntries = true)
    public SaleOrderResponse create(SaleOrderDTO dto) {
        log.info("Create sale order");

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        Client client = null;
        if (dto.getClientId() != null) {
            client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));
        }

        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        SaleOrder entity = mapper.toEntity(dto);
        entity.setClient(client);
        entity.setTotalSum(dto.getTotalSum());
        entity.setWarehouse(warehouse);
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedUsername(username);
        entity.setSalesOrderStatus(SalesOrderStatus.NEW);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
//    @Cacheable(value = "saleOrder", key = "#id")
    public SaleOrderResponse findById(Long id) {
        log.info("Fetch sale order by id {}", id);

        SaleOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
//    @Cacheable(value = "saleOrders")
    public Page<SaleOrderResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all sale orders");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderResponse> fetchByClientId(Long clientId, Pageable pageable) {
        log.info("Fetch sale orders by client id {}", clientId);

        return repository.findByClientId(clientId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderResponse> fetchByWarehouseId(Long warehouseId, Pageable pageable) {
        log.info("Fetch sale orders by warehouse id {}", warehouseId);

        return repository.findByWarehouseId(warehouseId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<SaleOrderResponse> fetchByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetch sale orders by date range {} - {}", startDate, endDate);

        return repository.findByOrderDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "SaleOrder"
    )
//    @CacheEvict(value = {"saleOrders", "saleOrder"}, allEntries = true)
    public SaleOrderResponse update(Long id, SaleOrderDTO dto) {
        log.info("Update sale order with id {}", id);

        SaleOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + id));

        if (dto.getWarehouseId() != null && !dto.getWarehouseId().equals(entity.getWarehouse().getId())) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));
            entity.setWarehouse(warehouse);
        }

        if (dto.getClientId() != null && (entity.getClient() == null || !dto.getClientId().equals(entity.getClient().getId()))) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));
            entity.setClient(client);
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
            entity = "SaleOrder"
    )
//    @CacheEvict(value = {"saleOrders", "saleOrder"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete sale order with id {}", id);

        SaleOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "SaleOrder"
    )
    public void changeStatus(Long id, SalesOrderStatus salesOrderStatus) {
        log.info("Change order {} status to {}", id, salesOrderStatus);
        SaleOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + id));

        if (entity.getSalesOrderStatus().equals(SalesOrderStatus.COMPLETED)) {
            throw new BadRequestException(
                    "Cannot change order status from " + entity.getSalesOrderStatus() + " to " + salesOrderStatus);
        }
        entity.setSalesOrderStatus(salesOrderStatus);
        repository.save(entity);
    }
}