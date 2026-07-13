package uz.script.wincrm.sale.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.enums.DebtNotificationStatus;
import uz.script.wincrm.sale.mapper.DebtNotificationHistoryMapper;
import uz.script.wincrm.sale.repository.DebtNotificationHistoryRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.response.DebtNotificationHistoryResponse;
import uz.script.wincrm.sale.response.DebtorClientResponse;
import uz.script.wincrm.sale.service.DebtNotificationService;
import uz.script.wincrm.sms.SmsSendException;
import uz.script.wincrm.sms.SmsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DebtNotificationServiceImpl implements DebtNotificationService {

    private final SaleOrderRepository saleOrderRepository;
    private final SmsService smsService;
    private final DebtNotificationHistoryRepository historyRepository;
    private final DebtNotificationHistoryMapper historyMapper;
    private final DebtNotificationHistoryRecorder historyRecorder;

    @Override
    public List<DebtorClientResponse> fetchDebtorClients(LocalDate startDate, LocalDate endDate, Long userId) {
        log.info("Fetch debtor clients. startDate={}, endDate={}, userId={}", startDate, endDate, userId);

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        if (start != null && end != null && start.isAfter(end)) {
            throw new BadRequestException("startDate endDate'dan katta bo'lishi mumkin emas");
        }

        List<SaleOrder> debtOrders = saleOrderRepository.findDebtOrders(start, end, userId);

        Map<Client, List<SaleOrder>> ordersByClient = debtOrders.stream()
                .filter(order -> order.getClient() != null)
                .collect(Collectors.groupingBy(SaleOrder::getClient));

        return ordersByClient.entrySet().stream()
                .map(entry -> toDebtorClientResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public void sendDebtNotificationToClient(Long clientId) {
        log.info("Send debt SMS to client id {}", clientId);

        List<SaleOrder> orders = saleOrderRepository.findByClient_IdAndDebtSumGreaterThan(clientId, BigDecimal.ZERO);

        if (orders.isEmpty()) {
            throw new BadRequestException("Ushbu mijozda qarz mavjud emas");
        }

        Client client = orders.get(0).getClient();
        sendToClient(client, orders);
    }

    @Override
    public void sendDebtNotificationsToClients(List<Long> clientIds) {
        log.info("Send debt SMS to {} selected clients", clientIds.size());

        for (Long clientId : clientIds) {
            try {
                sendDebtNotificationToClient(clientId);
            } catch (Exception e) {
                // Bitta mijozda xatolik bo'lsa ham, qolganlariga yuborishda davom etamiz
                log.error("Mijoz {} uchun SMS yuborishda xatolik: {}", clientId, e.getMessage());
            }
        }
    }

    @Override
    public void sendDebtNotificationForOrder(Long saleOrderId) {
        log.info("Send debt SMS for sale order id {}", saleOrderId);

        SaleOrder order = saleOrderRepository.findById(saleOrderId)
                .orElseThrow(() -> new BadRequestException("Sale order not found with id: " + saleOrderId));

        if (order.getClient() == null) {
            throw new BadRequestException("Ushbu buyurtmada mijoz biriktirilmagan");
        }

        if (order.getDebtSum() == null || order.getDebtSum().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Ushbu buyurtmada qarz mavjud emas");
        }

        sendToClient(order.getClient(), List.of(order));
    }

    @Override
    public Page<DebtNotificationHistoryResponse> fetchHistory(Pageable pageable) {
        log.info("Fetch debt notification history");

        return historyRepository.findAll(pageable)
                .map(historyMapper::toResponse);
    }

    @Override
    public Page<DebtNotificationHistoryResponse> fetchHistoryByClientId(Long clientId, Pageable pageable) {
        log.info("Fetch debt notification history for client id {}", clientId);

        return historyRepository.findByClientId(clientId, pageable)
                .map(historyMapper::toResponse);
    }

    private void sendToClient(Client client, List<SaleOrder> orders) {
        if (client.getPhone() == null || client.getPhone().isBlank()) {
            throw new BadRequestException("Mijoz '" + client.getFullName() + "' uchun telefon raqami mavjud emas");
        }

        BigDecimal totalDebt = orders.stream()
                .map(SaleOrder::getDebtSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String message = buildClientMessage(client, totalDebt);
//        String message = deafultTemplate();
        try {
            smsService.sendSms(client.getPhone(), message);
            historyRecorder.record(client, client.getPhone(), message, totalDebt, DebtNotificationStatus.SUCCESS, null);
        } catch (SmsSendException e) {
            historyRecorder.record(client, client.getPhone(), message, totalDebt, DebtNotificationStatus.FAILED, e.getMessage());
            throw e;
        }
    }

    private String deafultTemplate(){
        return "Assalomu allaykum ABAT STEKLO Kompanyasi Xodimlari bugun hammaga 17:30 majlis elon qilindi.";
    }

    private String buildClientMessage(Client client, BigDecimal totalDebt) {
//        return "Hurmatli " + client.getFullName() + ", sizning qarzingiz: " + totalDebt
//                + " so'm. Iltimos, to'lovni imkon qadar tezroq amalga oshiring.";
        return "Hurmatli" + client.getFullName()+ "! ABADSTEKLO korxonasidagi qarzingiz "+totalDebt+" soʻmni tashkil etadi." +
                " Iltimos, toʻlovni oʻz vaqtida amalga oshiring. Rahmat!";
    }

    private DebtorClientResponse toDebtorClientResponse(Client client, List<SaleOrder> orders) {
        BigDecimal totalDebt = orders.stream()
                .map(SaleOrder::getDebtSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DebtorClientResponse.DebtOrderInfo> orderInfos = orders.stream()
                .map(order -> DebtorClientResponse.DebtOrderInfo.builder()
                        .saleOrderId(order.getId())
                        .debtSum(order.getDebtSum())
                        .orderDate(order.getOrderDate())
                        .userId(order.getUser() != null ? order.getUser().getId() : null)
                        .userFullName(order.getUser() != null ? order.getUser().getFullName() : null)
                        .build())
                .toList();

        return DebtorClientResponse.builder()
                .clientId(client.getId())
                .clientFullName(client.getFullName())
                .phone(client.getPhone())
                .totalDebt(totalDebt)
                .orders(orderInfos)
                .build();
    }
}