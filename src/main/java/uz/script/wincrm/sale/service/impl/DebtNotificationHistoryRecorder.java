package uz.script.wincrm.sale.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.sale.DebtNotificationHistory;
import uz.script.wincrm.sale.enums.DebtNotificationStatus;
import uz.script.wincrm.sale.repository.DebtNotificationHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SMS tarixini YOZUV UCHUN ALOHIDA (REQUIRES_NEW) tranzaksiyada saqlaydi.
 * Bu shart, chunki asosiy sendToClient() metodi ichida SMS yuborish
 * muvaffaqiyatsiz bo'lsa, biz istisno (exception) otamiz — agar tarix
 * yozuvi o'sha bir xil tranzaksiyada bo'lsa, u ham rollback bo'lib
 * ketadi va FAILED holat bazaga hech qachon yozilmaydi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DebtNotificationHistoryRecorder {

    private final DebtNotificationHistoryRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(
            Client client,
            String phone,
            String message,
            BigDecimal totalDebtAmount,
            DebtNotificationStatus status,
            String errorMessage
    ) {
        DebtNotificationHistory history = DebtNotificationHistory.builder()
                .client(client)
                .phone(phone)
                .message(message)
                .totalDebtAmount(totalDebtAmount)
                .sentAt(LocalDateTime.now())
                .debtNotificationStatus(status)
                .errorMessage(errorMessage)
                .build();

        repository.save(history);

        log.info("Debt notification history saved: client={}, status={}", client.getId(), status);
    }
}