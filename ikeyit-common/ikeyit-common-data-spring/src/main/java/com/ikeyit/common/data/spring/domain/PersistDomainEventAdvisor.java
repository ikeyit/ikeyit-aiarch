package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.PersistDomainEvent;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.Ordered;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * An AOP advisor that manages the persistence of domain events in a transactional context.
 * This advisor intercepts methods annotated with both @TransactionalEventListener and @PersistDomainEvent
 * to ensure proper persistence and cleanup of domain events during transaction processing.
 */
public class PersistDomainEventAdvisor extends AbstractPointcutAdvisor {
    private static final Logger log = LoggerFactory.getLogger(PersistDomainEventAdvisor.class);
    private final DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory;
    private final Pointcut pointcut;
    private final Advice advice;
    /**
     * Constructs a new PersistDomainEventAdvisor.
     *
     * @param domainTransactionalEventListenerFactory The factory for creating transactional event listeners
     */
    public PersistDomainEventAdvisor(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory) {
        this.domainTransactionalEventListenerFactory = domainTransactionalEventListenerFactory;
        this.pointcut = new ComposablePointcut(new AnnotationMethodMatcher(TransactionalEventListener.class, true))
            .intersection(new AnnotationMethodMatcher(PersistDomainEvent.class, true));
        this.advice = new PersistMethodInterceptor();

    }

    /**
     * Gets the pointcut that identifies methods to be intercepted.
     *
     * @return The pointcut targeting methods with both @TransactionalEventListener and @PersistDomainEvent annotations
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
     * Method interceptor that handles the persistence and cleanup of domain events.
     * Implements Ordered to ensure highest precedence in the interceptor chain.
     */
    class PersistMethodInterceptor implements MethodInterceptor, Ordered {

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
         * Intercepts method calls to handle domain event persistence and cleanup.
         * Executes the intercepted method and manages the event lifecycle through the listener.
         *
         * @param invocation The method invocation being intercepted
         * @return The result of the method invocation
         * @throws Throwable If an error occurs during method invocation
         */
        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            try {
                Object result = invocation.proceed();
                Method method = invocation.getMethod();
                Object argument = invocation.getArguments()[0];
                PersistTransactionalApplicationListener listener = domainTransactionalEventListenerFactory.getListener(method);
                if (listener != null) {
                    DomainEventRepository domainEventRepository = ExecutionContext.replaceRepository();
                    if (domainEventRepository == null) {
                        listener.completeEvent(argument);
                    } else {
                        listener.completeEvent(argument, domainEventRepository);
                    }
                }
                return result;
            } catch (Throwable t) {
                log.error("Event is not completed! Persisted events are not removed!");
                throw t;
            }
        }
    }
}
