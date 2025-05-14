package com.ikeyit.access.infrastructure.listener;

import com.ikeyit.access.domain.event.InvitationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccessInvitationSender {

    private static final Logger log = LoggerFactory.getLogger(AccessInvitationSender.class);
    record CreativeCommand(String receiver, Long creativeId, Map<String, Object> params, long sendTime, String source){};

    private final StreamBridge streamBridge;

    @Value("${ikeyit.access.invitation.creativeId:0}")
    private final Long creativeId = 1L;

    public AccessInvitationSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @EventListener(id = "sendEmailOnInvitationCreated")
    @Async("accessTaskExecutor")
    public void sendEmailOnInvitationCreated(InvitationCreatedEvent event) {
        log.info("Send invitation email to {}", event.getEmail());
        CreativeCommand creativeCommand = new CreativeCommand(
            event.getEmail(),
            creativeId,
            Map.of(
                "invitationId", event.getInvitationId(),
                "orgName", event.getOrgName(),
                "roleName", event.getRoleName(),
                "inviterName", event.getInviterName(),
                "inviterId", event.getInviterId()
            ),
            System.currentTimeMillis(),
            "access"
        );
        streamBridge.send("messengerSendCreative-out-0", MessageBuilder.withPayload(creativeCommand).build());
    }
}
