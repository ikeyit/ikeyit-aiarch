package com.ikeyit.account.infrastructure.config;

import com.ikeyit.security.code.core.*;
import com.ikeyit.security.code.redis.RedisVerificationCodeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration(proxyBeanMethods = false)
public class AccountVerificationCodeConfig {

    @Bean
    public VerificationCodeGenerator verificationCodeGenerator() {
        return new DefaultVerificationCodeGenerator();
    }

    @Bean
    public VerificationCodeService loginEmailVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                     VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:login:email:"),
            new ConsoleVerificationCodeSender("loginEmailVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

    @Bean
    public VerificationCodeService loginPhoneVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                      VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:login:phone:"),
            new ConsoleVerificationCodeSender("loginPhoneVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

    @Bean
    public VerificationCodeService signupEmailVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                     VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:signup:email:"),
            new ConsoleVerificationCodeSender("signupEmailVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

    @Bean
    public VerificationCodeService signupPhoneVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                      VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:signup:phone:"),
            new ConsoleVerificationCodeSender("signupPhoneVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

    @Bean
    public VerificationCodeService updateEmailVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                      VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:update:email:"),
            new ConsoleVerificationCodeSender("updateEmailVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

    @Bean
    public VerificationCodeService updatePhoneVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                       VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:update:phone:"),
            new ConsoleVerificationCodeSender("updatePhoneVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

}
