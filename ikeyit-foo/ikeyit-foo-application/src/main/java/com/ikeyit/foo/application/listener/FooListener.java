package com.ikeyit.foo.application.listener;

import com.ikeyit.foo.domain.event.FooCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <pre>
 * === AI-NOTE ===
 * - Put code in the event listeners to keep it decoupled
 * - The listeners in the application layer are only for internal logic.
 * - The listeners are executed in the same JVM with the event publishers.
 * === AI-NOTE-END ===
 * </pre>
 **/
@Component
public class FooListener {
    
    private static final Logger logger = LoggerFactory.getLogger(FooListener.class);

    /**
     * Called when a FooCreatedEvent is published.
     */
    @EventListener
    public void onFooCreated(FooCreatedEvent event) {
        logger.info("onFooCreated: {}", event);
    }

    /**
     * Called when a FooCreatedEvent is published after transaction is committed
     */
    @TransactionalEventListener
    public void onFooCreatedAfterCommiting(FooCreatedEvent event) {
        logger.info("Executed when onFooCreated after transaction is committed: {}", event);
    }
}
