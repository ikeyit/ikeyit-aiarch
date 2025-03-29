package com.ikeyit.common.data.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that generate domain events that should be persisted.
 * This runtime annotation is used to indicate that the domain events produced by the annotated method
 * should be stored in a persistent storage (e.g., database) for event sourcing or audit purposes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface PersistDomainEvent {
    /**
     * Specifies the repository bean name to be used for persisting the domain event.
     * If not specified, a default repository will be used.
     *
     * @return The name of the repository bean to use for persistence
     */
    String repository() default "";
}
