package uz.script.wincrm.clients.service;

import uz.script.wincrm.clients.dto.ClientDTO;
import uz.script.wincrm.clients.response.ClientResponse;

import java.util.List;

public interface ClientService {
    boolean existsByPhone(String phone);
    boolean existsByInn(String inn);
    ClientResponse create(ClientDTO dto);
    ClientResponse findById(Long id);
    List<ClientResponse> fetchAllClients();
    ClientResponse update(Long id, ClientDTO dto);
    void delete(Long id);
}
