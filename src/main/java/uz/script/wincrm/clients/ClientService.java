package uz.script.wincrm.clients;

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
