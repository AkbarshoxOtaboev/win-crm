package uz.script.wincrm.clients.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.clients.ClientGroup;
import uz.script.wincrm.clients.dto.ClientGroupDTO;
import uz.script.wincrm.clients.repository.ClientGroupRepository;
import uz.script.wincrm.clients.response.ClientGroupResponse;
import uz.script.wincrm.clients.service.ClientGroupService;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientGroupServiceImplement implements ClientGroupService {

    private final ClientGroupRepository repository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Client Group"
    )
    public ClientGroupResponse create(ClientGroupDTO dto) {

        log.info("Create client group");

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new AlreadyExistsException(
                    "Client group with name '" + dto.getName() + "' already exists");
        }

        ClientGroup clientGroup = ClientGroup.builder()
                .name(dto.getName())
                .status(Status.ACTIVE)
                .build();

        clientGroup = repository.save(clientGroup);

        return map(clientGroup);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Client Group"
    )
    public ClientGroupResponse update(Long id, ClientGroupDTO dto) {

        log.info("Update client group with id {}", id);

        ClientGroup clientGroup = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client group not found with id: " + id));

        if (!clientGroup.getName().equalsIgnoreCase(dto.getName())
                && repository.existsByNameIgnoreCase(dto.getName())) {

            throw new AlreadyExistsException(
                    "Client group with name '" + dto.getName() + "' already exists");
        }

        clientGroup.setName(dto.getName());

        clientGroup = repository.save(clientGroup);

        return map(clientGroup);
    }

    @Override
    public ClientGroupResponse findById(Long id) {

        log.info("Fetch client group by id {}", id);

        ClientGroup clientGroup = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client group not found with id: " + id));

        return map(clientGroup);
    }

    @Override
    public List<ClientGroupResponse> findAll() {

        log.info("Fetch all client groups");

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Client Group"
    )
    public void delete(Long id) {

        log.info("Delete client group with id {}", id);

        ClientGroup clientGroup = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client group not found with id: " + id));

        clientGroup.setStatus(Status.DELETED);

        repository.save(clientGroup);
    }

    private ClientGroupResponse map(ClientGroup clientGroup) {

        return ClientGroupResponse.builder()
                .id(clientGroup.getId())
                .name(clientGroup.getName())
                .status(clientGroup.getStatus())
                .createdAt(clientGroup.getCreatedAt())
                .updatedAt(clientGroup.getUpdatedAt())
                .createdUsername(clientGroup.getCreatedUsername())
                .build();
    }
}