package uz.script.wincrm.clients.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.clients.ClientBalance;
import uz.script.wincrm.clients.dto.ClientBalanceDTO;
import uz.script.wincrm.clients.mapper.ClientBalanceMapper;
import uz.script.wincrm.clients.repository.ClientBalanceRepository;
import uz.script.wincrm.clients.repository.ClientRepository;
import uz.script.wincrm.clients.response.ClientBalanceResponse;
import uz.script.wincrm.clients.service.ClientBalanceService;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.payment.repository.PaymentRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

/*
 * DIQQAT: ResourceNotFoundException, PaymentRepository va SaleOrderRepository import yo'llari
 * loyihangizdagi haqiqiy paket nomlariga moslab tekshirib chiqing (bular ClientRepository bilan
 * bir xil naming konventsiyasi asosida taxmin qilindi).
 */
@Service
@RequiredArgsConstructor
public class ClientBalanceServiceImpl implements ClientBalanceService {

    private final ClientBalanceRepository balanceRepository;
    private final ClientRepository clientRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClientBalanceResponse> fetchAll(LocalDate fromDate, LocalDate toDate) {
        LocalDate resolvedFrom = resolveFromDate(fromDate);
        LocalDate resolvedTo = resolveToDate(toDate);
        LocalDateTime fromDateTime = resolvedFrom.atStartOfDay();
        LocalDateTime toDateTime = resolvedTo.atTime(LocalTime.MAX);

        // DIQQAT: har bir client uchun 2 tadan qo'shimcha SUM so'rovi bajariladi (N+1).
        // Client soni juda ko'p bo'lib, performance muammosi chiqsa, buni bitta GROUP BY
        // so'rovga (clientId -> summa map) almashtirish tavsiya etiladi.
        return balanceRepository.findAll()
                .stream()
                .map(balance -> {
                    Long clientId = balance.getClient().getId();

                    BigDecimal periodPurchase = saleOrderRepository.sumTotalSumByClientIdAndDateRange(
                            clientId, fromDateTime, toDateTime);
                    BigDecimal periodPaid = paymentRepository.sumPaymentAmountByClientIdAndDateRange(
                            clientId, fromDateTime, toDateTime);

                    return ClientBalanceResponse.builder()
                            .id(balance.getId())
                            .clientId(clientId)
                            .clientFullName(balance.getClient().getFullName())
                            .totalPurchase(periodPurchase)
                            .totalPaid(periodPaid)
                            .totalDebt(periodPurchase.subtract(periodPaid))
                            .lastUpdated(balance.getLastUpdated())
                            .periodFrom(resolvedFrom)
                            .periodTo(resolvedTo)
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientBalanceResponse findByClientId(Long clientId, LocalDate fromDate, LocalDate toDate) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        LocalDate resolvedFrom = resolveFromDate(fromDate);
        LocalDate resolvedTo = resolveToDate(toDate);

        BigDecimal periodPurchase = saleOrderRepository.sumTotalSumByClientIdAndDateRange(
                clientId, resolvedFrom.atStartOfDay(), resolvedTo.atTime(LocalTime.MAX));
        BigDecimal periodPaid = paymentRepository.sumPaymentAmountByClientIdAndDateRange(
                clientId, resolvedFrom.atStartOfDay(), resolvedTo.atTime(LocalTime.MAX));

        // id/lastUpdated - ixtiyoriy, faqat mavjud bo'lsa (barcha vaqt balansi hali yaratilmagan bo'lishi mumkin)
        ClientBalance balance = balanceRepository.findByClient_Id(clientId).orElse(null);

        return ClientBalanceResponse.builder()
                .id(balance != null ? balance.getId() : null)
                .clientId(client.getId())
                .clientFullName(client.getFullName())
                .totalPurchase(periodPurchase)
                .totalPaid(periodPaid)
                .totalDebt(periodPurchase.subtract(periodPaid))
                .lastUpdated(balance != null ? balance.getLastUpdated() : null)
                .periodFrom(resolvedFrom)
                .periodTo(resolvedTo)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientBalanceResponse findAllTimeByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        return balanceRepository.findByClient_Id(clientId)
                .map(ClientBalanceMapper::toResponse)
                .orElseGet(() -> ClientBalanceResponse.builder()
                        .clientId(client.getId())
                        .clientFullName(client.getFullName())
                        .totalPurchase(BigDecimal.ZERO)
                        .totalPaid(BigDecimal.ZERO)
                        .totalDebt(BigDecimal.ZERO)
                        .build());
    }

    private LocalDate resolveFromDate(LocalDate fromDate) {
        return fromDate != null ? fromDate : YearMonth.now().atDay(1);
    }

    private LocalDate resolveToDate(LocalDate toDate) {
        return toDate != null ? toDate : YearMonth.now().atEndOfMonth();
    }

    @Override
    @Transactional
    public void recalculateClientBalance(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        ClientBalance balance = balanceRepository.findByClient_Id(clientId)
                .orElseGet(() -> ClientBalance.builder()
                        .client(client)
                        .totalPurchase(BigDecimal.ZERO)
                        .totalPaid(BigDecimal.ZERO)
                        .totalDebt(BigDecimal.ZERO)
                        .build());

        BigDecimal totalPurchase = saleOrderRepository.sumTotalSumByClientId(clientId);
        BigDecimal totalPaid = paymentRepository.sumPaymentAmountByClientId(clientId);

        balance.setTotalPurchase(totalPurchase);
        balance.setTotalPaid(totalPaid);
        balance.setTotalDebt(totalPurchase.subtract(totalPaid));
        balance.setLastUpdated(LocalDateTime.now());

        balanceRepository.save(balance);
    }

    @Override
    @Transactional
    public ClientBalanceResponse adjustBalance(Long clientId, ClientBalanceDTO dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        ClientBalance balance = balanceRepository.findByClient_Id(clientId)
                .orElseGet(() -> ClientBalance.builder()
                        .client(client)
                        .build());

        balance.setTotalPurchase(dto.getTotalPurchase());
        balance.setTotalPaid(dto.getTotalPaid());
        balance.setTotalDebt(dto.getTotalPurchase().subtract(dto.getTotalPaid()));
        balance.setLastUpdated(LocalDateTime.now());

        return ClientBalanceMapper.toResponse(balanceRepository.save(balance));
    }
}