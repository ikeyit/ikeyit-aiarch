package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.DomainEvent;
import com.ikeyit.common.data.domain.DomainEventSupplier;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;


/**
 * A Spring AOP advisor that handles the automatic publishing of domain events.
 * This advisor intercepts methods annotated with @PublishDomainEvent and publishes
 * domain events from method arguments that implement DomainEventSupplier.
 */
public class PublishDomainEventAdvisor extends AbstractPointcutAdvisor {
    private static final Logger log = LoggerFactory.getLogger(PublishDomainEventAdvisor.class);
    private final ApplicationContext applicationContext;
    private final Pointcut pointcut;
    private final Advice advice;

    /**
     * Creates a new PublishDomainEventAdvisor.
     * 
     * @param applicationContext The Spring application context used to publish events
     */
    public PublishDomainEventAdvisor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.pointcut = new ComposablePointcut(new AnnotationMethodMatcher(PublishDomainEvent.class, true));
        this.advice = new PublishMethodInterceptor();
    }

    /**
     * Gets the pointcut that identifies methods to be intercepted.
     * 
     * @return The pointcut targeting methods annotated with @PublishDomainEvent
     */
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    /**
     * Gets the advice that handles the interception.
     * 
     * @return The method interceptor advice
     */
    @Override
    public Advice getAdvice() {
        return advice;
    }

    /**
     * Method interceptor that handles the actual publishing of domain events.
     * Implements Ordered to ensure highest precedence in the interceptor chain.
     */
    class PublishMethodInterceptor implements MethodInterceptor, Ordered {

        /**
         * Defines the order of this interceptor in the interceptor chain.
         * 
         * @return HIGHEST_PRECEDENCE to ensure this interceptor runs first
         */
        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        /**
         * Intercepts method calls and publishes domain events from the method arguments.
         * Handles both individual DomainEventSupplier instances and collections of them.
         * 
         * @param invocation The method invocation being intercepted
         * @return The result of the method invocation
         * @throws Throwable If an error occurs during method invocation
         */
        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            Object result = invocation.proceed();
            for (Object arg : invocation.getArguments()) {
                if (arg instanceof DomainEventSupplier domainEventSupplier) {
                    publish(domainEventSupplier);
                } else if (arg instanceof Collection<?> domainEventSupplierCollection) {
                    domainEventSupplierCollection.forEach(item -> {
                        if (item instanceof DomainEventSupplier domainEventSupplier) {
                            publish(domainEventSupplier);
                        }
                    });
                }
            }
            return result;
        }

        private void publish(DomainEventSupplier domainEventSupplier) {
            for (DomainEvent event : domainEventSupplier.domainEvents()) {
                applicationContext.publishEvent(event);
                log.debug("Publish domain event: {}", event);
            }
            domainEventSupplier.clearDomainEvents();
        }
    }
}
