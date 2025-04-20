package com.ikeyit.account.interfaces.api.auth.authsession;

import com.ikeyit.common.data.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class RedisAuthSessionRepository implements AuthSessionRepository {

    private final String redisKeyPrefix = "account:authsession:";

    private final StringRedisTemplate redisTemplate;

    public RedisAuthSessionRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(AuthSession session) {
        Duration duration = Duration.between(Instant.now(), session.getExpiresAt());
        redisTemplate.opsForValue().set(
            redisKeyPrefix + session.getId(),
            JsonUtils.writeValueAsString(session),
            duration);
    }

    @Override
    public AuthSession findById(String id) {
        String value = redisTemplate.opsForValue().get(redisKeyPrefix + id);
        if (value == null) {
            return null;
        }
        return JsonUtils.readValue(value, AuthSession.class);
    }

    @Override
    public List<AuthSession> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public void deleteById(String id) {
        redisTemplate.delete(redisKeyPrefix + id);
    }
}
