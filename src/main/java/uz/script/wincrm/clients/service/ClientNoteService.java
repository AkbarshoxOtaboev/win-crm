package uz.script.wincrm.clients.service;

import uz.script.wincrm.clients.enums.ReminderStatus;
import uz.script.wincrm.clients.dto.ClientNoteDTO;
import uz.script.wincrm.clients.response.ClientNoteResponse;

import java.util.List;

public interface ClientNoteService {
    ClientNoteResponse create(ClientNoteDTO dto);
    ClientNoteResponse findById(Long id);
    List<ClientNoteResponse> fetchByClient(Long clientId);
    List<ClientNoteResponse> fetchDueReminders();
    ClientNoteResponse updateReminderStatus(Long id, ReminderStatus status);
    ClientNoteResponse update(Long id, ClientNoteDTO dto);
    void delete(Long id);
}