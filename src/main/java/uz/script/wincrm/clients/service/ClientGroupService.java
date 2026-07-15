package uz.script.wincrm.clients.service;

import uz.script.wincrm.clients.dto.ClientGroupDTO;
import uz.script.wincrm.clients.response.ClientGroupResponse;

import java.util.List;

public interface ClientGroupService {

    ClientGroupResponse create(ClientGroupDTO dto);

    ClientGroupResponse update(Long id, ClientGroupDTO dto);

    ClientGroupResponse findById(Long id);

    List<ClientGroupResponse> findAll();

    void delete(Long id);
}
