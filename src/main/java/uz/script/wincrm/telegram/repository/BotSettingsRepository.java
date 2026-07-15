package uz.script.wincrm.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.telegram.BotSettings;

import java.util.Optional;

@Repository
public interface BotSettingsRepository extends JpaRepository<BotSettings, Long> {

    Optional<BotSettings> findFirstByActiveTrue();
}