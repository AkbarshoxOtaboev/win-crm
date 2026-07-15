package uz.script.wincrm.telegram.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bot ko'p instansiyali (masalan bir nechta server) muhitda ishlashi kerak
 * bo'lsa, bu klassni Redisga ko'chirish tavsiya etiladi (loyihada Redis cache
 * allaqachon sozlangan). Hozircha oddiy suhbatlar (register -> menu -> akt)
 * uchun in-memory yetarli.
 */
@Component
public class BotSessionService {

    private final Map<Long, BotConversationState> states = new ConcurrentHashMap<>();

    public BotConversationState getState(Long chatId) {
        return states.getOrDefault(chatId, BotConversationState.AWAITING_PHONE);
    }

    public void setState(Long chatId, BotConversationState state) {
        states.put(chatId, state);
    }

    public void clear(Long chatId) {
        states.remove(chatId);
    }
}