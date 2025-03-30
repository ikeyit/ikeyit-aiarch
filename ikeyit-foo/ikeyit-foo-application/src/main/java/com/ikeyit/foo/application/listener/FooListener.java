package com.ikeyit.foo.application.listener;

import com.ikeyit.foo.domain.event.FooCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FooListener {
    private static final Logger logger = LoggerFactory.getLogger(FooListener.class);
    public void onFooCreated(FooCreatedEvent event) {
        logger.info("onFooCreated: {}", event);
    }
}
