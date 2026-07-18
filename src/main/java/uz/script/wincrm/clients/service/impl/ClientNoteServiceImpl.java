package uz.script.wincrm.clients.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.clients.ClientNote;
import uz.script.wincrm.clients.enums.ReminderStatus;
import uz.script.wincrm.clients.dto.ClientNoteDTO;
import uz.script.wincrm.clients.mapper.ClientNoteMapper;
import uz.script.wincrm.clients.repository.ClientNoteRepository;
import uz.script.wincrm.clients.repository.ClientRepository;
import uz.script.wincrm.clients.response.ClientNoteResponse;
import uz.script.wincrm.clients.service.ClientNoteService;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.utils.Status;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientNoteServiceImpl implements ClientNoteService {

    private final ClientNoteRepository repository;
    private final ClientRepository clientRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "ClientNote"
    )
    public ClientNoteResponse create(ClientNoteDTO dto) {
        log.info("Create client note for client {}", dto.getClientId());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id: " + dto.getClientId()));

        ClientNote note = ClientNote.builder()
                .client(client)
                .saleOrderId(dto.getSaleOrderId())
                .type(dto.getType())
                .content(dto.getContent())
                .interactionDate(dto.getInteractionDate())
                .reminderDate(dto.getReminderDate())
                // reminderDate berilgan bo'lsa avtomatik PENDING, aks holda NONE
                .reminderStatus(dto.getReminderDate() != null
                        ? ReminderStatus.PENDING
                        : ReminderStatus.NONE)
                .promisedAmount(dto.getPromisedAmount())
                .status(Status.ACTIVE)
                .build();

        note = repository.save(note);

        return ClientNoteMapper.toResponse(note);
    }

    @Override
    public ClientNoteResponse findById(Long id) {
        log.info("Fetch client note by id {}", id);

        ClientNote note = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client note not found with id: " + id));

        return ClientNoteMapper.toResponse(note);
    }

    @Override
    public List<ClientNoteResponse> fetchByClient(Long clientId) {
        log.info("Fetch client notes for client {}", clientId);

        return repository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(ClientNoteMapper::toResponse)
                .toList();
    }

    @Override
    public List<ClientNoteResponse> fetchDueReminders() {
        log.info("Fetch due reminders as of {}", LocalDate.now());

        return repository.findByReminderDateLessThanEqualAndReminderStatus(
                        LocalDate.now(), ReminderStatus.PENDING)
                .stream()
                .map(ClientNoteMapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "ClientNote"
    )
    public ClientNoteResponse updateReminderStatus(Long id, ReminderStatus status) {
        log.info("Update reminder status of note {} to {}", id, status);

        ClientNote note = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client note not found with id: " + id));

        note.setReminderStatus(status);
        note = repository.save(note);

        return ClientNoteMapper.toResponse(note);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "ClientNote"
    )
    public ClientNoteResponse update(Long id, ClientNoteDTO dto) {
        log.info("Update client note with id {}", id);

        ClientNote note = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client note not found with id: " + id));

        note.setSaleOrderId(dto.getSaleOrderId());
        note.setType(dto.getType());
        note.setContent(dto.getContent());
        note.setInteractionDate(dto.getInteractionDate());
        note.setReminderDate(dto.getReminderDate());

        if (dto.getReminderStatus() != null) {
            note.setReminderStatus(dto.getReminderStatus());
        }

        note.setPromisedAmount(dto.getPromisedAmount());

        note = repository.save(note);

        return ClientNoteMapper.toResponse(note);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "ClientNote"
    )
    public void delete(Long id) {
        log.info("Delete client note with id {}", id);

        ClientNote note = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client note not found with id: " + id));

        note.setStatus(Status.DELETED);
        repository.save(note);
    }
}