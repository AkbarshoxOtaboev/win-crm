package uz.script.wincrm.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.clients.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByPhone(String phone);

    boolean existsByInn(String inn);
}
