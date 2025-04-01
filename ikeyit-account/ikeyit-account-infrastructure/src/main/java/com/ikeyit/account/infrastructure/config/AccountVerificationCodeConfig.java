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
    public VerificationCodeService loginMobileVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                      VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:login:mobile:"),
            new ConsoleVerificationCodeSender("loginMobileVerificationCodeSender"),
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
    public VerificationCodeService signupMobileVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                      VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:signup:mobile:"),
            new ConsoleVerificationCodeSender("signupMobileVerificationCodeSender"),
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
    public VerificationCodeService updateMobileVerificationCodeService(StringRedisTemplate redisTemplate,
                                                                       VerificationCodeGenerator verificationCodeGenerator) {
        return new DefaultVerificationCodeService(
            new RedisVerificationCodeRepository(redisTemplate, "account:vc:update:mobile:"),
            new ConsoleVerificationCodeSender("updateMobileVerificationCodeSender"),
            verificationCodeGenerator
        );
    }

}
