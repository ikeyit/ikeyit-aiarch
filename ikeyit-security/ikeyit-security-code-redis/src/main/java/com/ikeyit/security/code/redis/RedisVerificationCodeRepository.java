package com.ikeyit.security.code.redis;

import com.ikeyit.security.code.core.VerificationCode;
import com.ikeyit.security.code.core.VerificationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Redis implementation of VerificationCodeRepository
 */
public class RedisVerificationCodeRepository implements VerificationCodeRepository {

    private final static Logger log = LoggerFactory.getLogger(RedisVerificationCodeRepository.class);

    private final static String DEFAULT_KEY_PREFIX = "VC:";

    private final static String SEP = ",";

    private final StringRedisTemplate redisTemplate;

    private String keyPrefix ;

    public RedisVerificationCodeRepository(StringRedisTemplate redisTemplate) {
       this(redisTemplate, DEFAULT_KEY_PREFIX);
    }

    public RedisVerificationCodeRepository(StringRedisTemplate redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void save(VerificationCode verificationCode) {
        log.debug("Save verification code: {}", verificationCode);
        redisTemplate.boundValueOps(keyPrefix + verificationCode.getTarget())
                .set(serialize(verificationCode),
                Duration.between(LocalDateTime.now(), verificationCode.getExpireTime()));
    }

    @Override
    public VerificationCode get(String target) {
        String value = redisTemplate.boundValueOps(keyPrefix + target).get();
        VerificationCode verificationCode = deserialize(value);
        log.debug("Load target: {}, verification code: {}", target, verificationCode);
        return verificationCode;
    }

    @Override
    public void deleteByTarget(String target) {
        redisTemplate.delete(keyPrefix + target);
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    protected String serialize(VerificationCode verificationCode) {
        return String.join(SEP,
            verificationCode.getTarget(),
            verificationCode.getCode(),
            Long.toString(toEpochMilli(verificationCode.getExpireTime())),
            Long.toString(toEpochMilli(verificationCode.getCreateTime())));
    }

    protected VerificationCode deserialize(String value) {
        if (value == null)
            return null;
        String[] fields = value.split(SEP);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setTarget(fields[0]);
        verificationCode.setCode(fields[1]);
        verificationCode.setExpireTime(fromEpochMilli(fields[2]));
        verificationCode.setCreateTime(fromEpochMilli(fields[3]));
        return verificationCode;
    }

    private static long toEpochMilli(LocalDateTime dataTime) {
        return dataTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private LocalDateTime fromEpochMilli(String epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(epochMilli)), ZoneOffset.UTC);
    }
}
