package uz.script.wincrm.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.clients.ClientBalance;

import java.util.Optional;

@Repository
public interface ClientBalanceRepository extends JpaRepository<ClientBalance, Long> {

    Optional<ClientBalance> findByClient_Id(Long clientId);

    boolean existsByClient_Id(Long clientId);
}