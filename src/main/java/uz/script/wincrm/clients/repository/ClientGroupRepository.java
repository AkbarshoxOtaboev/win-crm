package uz.script.wincrm.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.script.wincrm.clients.ClientGroup;

public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {

    boolean existsByNameIgnoreCase(String name);
}