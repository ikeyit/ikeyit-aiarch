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

public class PersistDomainEventAdvisor extends AbstractPointcutAdvisor {
    private static final Logger log = LoggerFactory.getLogger(PersistDomainEventAdvisor.class);
    private final DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory;
    private final Pointcut pointcut;
    private final Advice advice;
    public PersistDomainEventAdvisor(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory) {
        this.domainTransactionalEventListenerFactory = domainTransactionalEventListenerFactory;
        this.pointcut = new ComposablePointcut(new AnnotationMethodMatcher(TransactionalEventListener.class, true))
            .intersection(new AnnotationMethodMatcher(PersistDomainEvent.class, true));
        this.advice = new PersistMethodInterceptor();

    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    class PersistMethodInterceptor implements MethodInterceptor, Ordered {

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

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
