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


public class PublishDomainEventAdvisor extends AbstractPointcutAdvisor {
    private static final Logger log = LoggerFactory.getLogger(PublishDomainEventAdvisor.class);
    private final ApplicationContext applicationContext;
    private final Pointcut pointcut;
    private final Advice advice;

    public PublishDomainEventAdvisor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.pointcut = new ComposablePointcut(new AnnotationMethodMatcher(PublishDomainEvent.class, true));
        this.advice = new PublishMethodInterceptor();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    class PublishMethodInterceptor implements MethodInterceptor, Ordered {

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

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
