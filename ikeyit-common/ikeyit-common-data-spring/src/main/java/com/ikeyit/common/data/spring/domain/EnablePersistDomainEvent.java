package com.ikeyit.common.data.spring.domain;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DomainTransactionalEventListenerFactory.class, PersistDomainEventAdvisor.class})
public @interface EnablePersistDomainEvent {
}