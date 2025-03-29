package com.ikeyit.common.data.spring.domain;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PublishDomainEventAdvisor.class})
/**
 * Enables automatic publishing of domain events in a Spring application.
 * When this annotation is applied to a configuration class, it enables the PublishDomainEventAdvisor
 * which intercepts methods annotated with @PublishDomainEvent and automatically publishes their domain events.
 */
public @interface EnablePublishDomainEvent {

}