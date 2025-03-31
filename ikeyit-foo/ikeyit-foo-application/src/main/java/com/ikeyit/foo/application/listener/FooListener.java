package com.ikeyit.foo.application.listener;

import com.ikeyit.foo.domain.event.FooCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Application event listeners in the application layer for internal usage.
 * These listeners will be executed in the same JVM
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
