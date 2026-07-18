package uz.script.wincrm.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.clients.ClientNote;
import uz.script.wincrm.clients.enums.ReminderStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientNoteRepository extends JpaRepository<ClientNote, Long> {

    List<ClientNote> findByClientIdOrderByCreatedAtDesc(Long clientId);

    // Aynan shu kunga rejalashtirilgan eslatmalar
    List<ClientNote> findByReminderDateAndReminderStatus(LocalDate reminderDate, ReminderStatus reminderStatus);

    // Muddati kelgan yoki o'tib ketgan, hali hal qilinmagan eslatmalar (dashboard/scheduler uchun)
    List<ClientNote> findByReminderDateLessThanEqualAndReminderStatus(LocalDate reminderDate, ReminderStatus reminderStatus);
}