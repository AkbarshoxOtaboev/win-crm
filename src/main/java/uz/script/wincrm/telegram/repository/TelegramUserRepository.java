package uz.script.wincrm.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.telegram.TelegramUser;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    Optional<TelegramUser> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    Optional<TelegramUser> findByPhone(String phone);

    boolean existsByPhone(String phone);

    /** Admin panelidan tanlangan mijozga xabar yuborishda uning chatId'sini topish uchun. */
    Optional<TelegramUser> findByClient_Id(Long clientId);
}