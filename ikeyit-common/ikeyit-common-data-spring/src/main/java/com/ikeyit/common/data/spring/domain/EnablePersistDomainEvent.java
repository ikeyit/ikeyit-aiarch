package com.ikeyit.common.data.spring.domain;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DomainTransactionalEventListenerFactory.class, PersistDomainEventAdvisor.class})
/**
 * Enables domain event persistence capabilities in a Spring application.
 * When this annotation is applied to a configuration class, it enables the PersistDomainEventAdvisor
 * and DomainTransactionalEventListenerFactory, which together provide the infrastructure for
 * persisting domain events during transaction processing.
 */
public @interface EnablePersistDomainEvent {
}