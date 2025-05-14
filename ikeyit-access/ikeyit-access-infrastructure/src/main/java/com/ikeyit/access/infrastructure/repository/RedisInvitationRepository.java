package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.common.data.JsonUtils;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.access.domain.model.Invitation;
import com.ikeyit.access.domain.repository.InvitationRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Repository
public class RedisInvitationRepository implements InvitationRepository {
    public static final String REDIS_KEY_PREFIX = "org-invitation:";

    private final StringRedisTemplate redisTemplate;

    private final SecureRandom secureRandom = new SecureRandom();

    public RedisInvitationRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<Invitation> findById(String id) {
        String data = redisTemplate.boundValueOps(REDIS_KEY_PREFIX + id).get();
        if (data == null)
            return Optional.empty();
        Invitation invitation = JsonUtils.readValue(data, Invitation.class);
        return Optional.ofNullable(invitation);
    }

    @Override
    @PublishDomainEvent
    public void create(Invitation entity) {
        entity.assignId(generateId());
        redisTemplate.boundValueOps(REDIS_KEY_PREFIX + entity.getId()).set(JsonUtils.writeValueAsString(entity), entity.getTimeout());
    }

    private String generateId() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Override
    public void update(Invitation entity) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void delete(Invitation entity) {
        redisTemplate.delete(REDIS_KEY_PREFIX + entity.getId());
    }
}
