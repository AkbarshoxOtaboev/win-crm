package uz.script.wincrm.security.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;

    public void blacklist(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(token, "blacklisted",
                Duration.ofMillis(expirationMillis));
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}