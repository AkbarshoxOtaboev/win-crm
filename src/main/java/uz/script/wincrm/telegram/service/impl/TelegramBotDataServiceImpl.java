package uz.script.wincrm.telegram.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.clients.ClientBalance;
import uz.script.wincrm.clients.repository.ClientBalanceRepository;
import uz.script.wincrm.payment.repository.PaymentRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.telegram.service.TelegramBotDataService;
import uz.script.wincrm.telegram.view.ClientBalanceView;
import uz.script.wincrm.telegram.view.PaymentView;
import uz.script.wincrm.telegram.view.SaleOrderView;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TelegramBotDataServiceImpl implements TelegramBotDataService {

    private final SaleOrderRepository saleOrderRepository;
    private final PaymentRepository paymentRepository;
    private final ClientBalanceRepository clientBalanceRepository;

    @Override
    public List<SaleOrderView> findOrdersByClientId(Long clientId) {
        return saleOrderRepository.findOrderViewsByClientId(clientId);
    }

    @Override
    public Optional<SaleOrderView> findOrderByIdAndClientId(Long orderId, Long clientId) {
        return saleOrderRepository.findOrderViewByIdAndClientId(orderId, clientId);
    }

    @Override
    public List<PaymentView> findPaymentsByClientId(Long clientId) {
        return paymentRepository.findPaymentViewsByClientId(clientId);
    }

    @Override
    public List<PaymentView> findPaymentsByOrderId(Long orderId) {
        return paymentRepository.findPaymentViewsBySaleOrderId(orderId);
    }

    @Override
    public Optional<ClientBalanceView> findBalanceByClientId(Long clientId) {
        return clientBalanceRepository.findByClient_Id(clientId).map(this::toView);
    }

    private ClientBalanceView toView(ClientBalance balance) {
        return new ClientBalanceView(
                balance.getTotalPurchase(),
                balance.getTotalPaid(),
                balance.getTotalDebt(),
                balance.getLastUpdated()
        );
    }
}