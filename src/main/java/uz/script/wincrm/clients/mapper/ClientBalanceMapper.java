package uz.script.wincrm.clients.mapper;

import uz.script.wincrm.clients.ClientBalance;
import uz.script.wincrm.clients.response.ClientBalanceResponse;

public class ClientBalanceMapper {

    private ClientBalanceMapper() {
    }

    public static ClientBalanceResponse toResponse(ClientBalance balance) {
        return ClientBalanceResponse.builder()
                .id(balance.getId())
                .clientId(balance.getClient().getId())
                .clientFullName(balance.getClient().getFullName())
                .totalPurchase(balance.getTotalPurchase())
                .totalPaid(balance.getTotalPaid())
                .totalDebt(balance.getTotalDebt())
                .lastUpdated(balance.getLastUpdated())
                .build();
    }
}