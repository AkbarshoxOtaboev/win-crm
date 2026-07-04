package uz.script.wincrm.clients;

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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Client"
    )
    @CacheEvict(value = "clients", allEntries = true)
    public ClientResponse create(ClientDTO dto) {
        log.info("Create client");

        if (existsByPhone(dto.getPhone())) {
            throw new AlreadyExistsException("Client with phone '" + dto.getPhone() + "' already exists");
        }
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        Client client = Client.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .status(Status.ACTIVE)
                .createdUsername(username)
                .build();

        client = repository.save(client);

        return mapClientToClientResponse(client);
    }

    @Override
    @Cacheable(value = "client", key = "#id")
    public ClientResponse findById(Long id) {
        log.info("Fetch client by id {}", id);

        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        return mapClientToClientResponse(client);
    }

    @Override
    @Cacheable(value = "clients")
    public List<ClientResponse> fetchAllClients() {
        log.info("Fetch all clients");

        return repository.findAll()
                .stream()
                .map(this::mapClientToClientResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Client"
    )
    @CacheEvict(value = {"clients", "client"}, allEntries = true)
    public ClientResponse update(Long id, ClientDTO dto) {
        log.info("Update client with id {}", id);

        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (!client.getPhone().equals(dto.getPhone()) && existsByPhone(dto.getPhone())) {
            throw new AlreadyExistsException("Client with phone '" + dto.getPhone() + "' already exists");
        }

        client.setFullName(dto.getFullName());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());

        return mapClientToClientResponse(client);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Client"
    )
    @CacheEvict(value = {"clients", "client"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete client with id {}", id);

        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        client.setStatus(Status.DELETED);
    }
    private ClientResponse mapClientToClientResponse(Client client){
        return ClientResponse.builder()
                .id(client.getId())
                .fullName(client.getFullName())
                .phone(client.getPhone())
                .address(client.getAddress())
                .status(client.getStatus())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .createdUsername(client.getCreatedUsername())
                .build();
    }
}
