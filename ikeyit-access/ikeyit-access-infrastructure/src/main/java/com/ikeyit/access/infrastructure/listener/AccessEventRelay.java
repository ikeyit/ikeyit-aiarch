package com.ikeyit.access.infrastructure.listener;

import com.ikeyit.access.domain.event.MemberActivatedEvent;
import com.ikeyit.access.domain.event.MemberCreatedEvent;
import com.ikeyit.common.data.domain.BaseDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class AccessEventRelay {
    private static final Logger log = LoggerFactory.getLogger(AccessEventRelay.class);

    private final StreamBridge streamBridge;

    public AccessEventRelay(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private void sendEventToMQ(String topic, BaseDomainEvent event) {
        log.debug("Relay event to MQ. Topic: {}, Event: {}, ", topic, event);
        streamBridge.send(topic, MessageBuilder.withPayload(event).build());
    }

    @EventRelayListener(id = "relayMemberCreatedEvent")
    public void relayMemberCreatedEvent(MemberCreatedEvent event) {
        sendEventToMQ("accessMemberCreated-out-0", event);
    }

    @EventRelayListener(id = "relayMemberActivatedEvent")
    public void relayMemberActivatedEvent(MemberActivatedEvent event) {
        sendEventToMQ("accessMemberActivated-out-0", event);
    }
}
