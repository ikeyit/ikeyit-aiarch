package com.ikeyit.access.infrastructure.listener;

import com.ikeyit.common.data.domain.PersistDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@TransactionalEventListener
@PersistDomainEvent(repository = "accessDomainEventRepository")
@Async("accessDomainEventExecutor")
@interface EventRelayListener {
    @AliasFor(annotation = EventListener.class, attribute = "id")
    String id() default "";
}
