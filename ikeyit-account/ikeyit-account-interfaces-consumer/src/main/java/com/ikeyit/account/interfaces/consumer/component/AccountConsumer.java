package com.ikeyit.account.interfaces.consumer.component;

import com.ikeyit.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.Instant;
import java.util.function.Consumer;

@Configuration(proxyBeanMethods = false)
public class AccountConsumer {

    private static final Logger log = LoggerFactory.getLogger(AccountConsumer.class);

    public AccountConsumer() {

    }

    @Bean
    public Consumer<Message<BigBangMessage>> accountConsumeBigBang() {
        return message -> {
            try {
                var actualMessage = message.getPayload();
                log.info("Received message from MQ: {}", actualMessage);
            } catch (BizException e) {
                log.error("Failed to consume message. The message will not be retried!", e);
            }
        };
    }

    public record BigBangMessage(Instant time) {
    }
}
