package com.ikeyit.common.data.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that generate domain events that should be published.
 * This runtime annotation indicates that the domain events produced by the annotated method
 * should be published to event listeners or message brokers for further processing.
 * This is typically used for implementing event-driven architectures and maintaining
 * eventual consistency across bounded contexts.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PublishDomainEvent {

}
