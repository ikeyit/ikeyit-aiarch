package com.ikeyit.foo.infrastructure.listener;

import com.ikeyit.common.data.domain.BaseDomainEvent;
import com.ikeyit.common.data.domain.PersistDomainEvent;
import com.ikeyit.foo.domain.event.FooCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <pre>
 * === AI-NOTE ===
 * - This class is used to relay domain events to external MQ
 * === AI-NOTE-END ===
 * </pre>
 */
@Component
public class FooEventRelay {
    private static final Logger log = LoggerFactory.getLogger(FooEventRelay.class);

    private final StreamBridge streamBridge;

    public FooEventRelay(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private void sendEventToMQ(String topic, BaseDomainEvent event) {
        log.debug("Relay event to MQ. Topic: {}, Event: {}, ", topic, event);
        // uncomment the line to send messages to a real MQ
//        streamBridge.send(topic, MessageBuilder.withPayload(event).build());
    }

    /**
     * <pre>
     * === AI-NOTE ===
     * - @TransactionalEventListener ensures the code is executed after the transaction is commited to database
     * - @PersistDomainEvent ensures the code is executed at least once, but may multiple times. Idempotence is required
     * - @Async makes the code execute in the async way.
     * === AI-NOTE-END ===
     * </pre>
     */
    @TransactionalEventListener
    @PersistDomainEvent(repository = "fooDomainEventRepository")
    @Async
    public void relayFooCreatedEvent(FooCreatedEvent event) {
        sendEventToMQ("fooCreated-out-0", event);
    }
}
