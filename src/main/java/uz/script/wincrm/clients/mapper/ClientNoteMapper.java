package uz.script.wincrm.clients.mapper;

import uz.script.wincrm.clients.ClientNote;
import uz.script.wincrm.clients.response.ClientNoteResponse;

public class ClientNoteMapper {

    private ClientNoteMapper() {
    }

    public static ClientNoteResponse toResponse(ClientNote note) {
        return ClientNoteResponse.builder()
                .id(note.getId())
                .clientId(note.getClient().getId())
                .clientFullName(note.getClient().getFullName())
                .saleOrderId(note.getSaleOrderId())
                .type(note.getType())
                .content(note.getContent())
                .interactionDate(note.getInteractionDate())
                .reminderDate(note.getReminderDate())
                .reminderStatus(note.getReminderStatus())
                .promisedAmount(note.getPromisedAmount())
                .status(note.getStatus())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .createdUsername(note.getCreatedUsername())
                .build();
    }
}