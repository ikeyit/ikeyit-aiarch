package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.PersistDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.RestrictedTransactionalEventListenerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DomainTransactionalEventListenerFactory extends RestrictedTransactionalEventListenerFactory implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(DomainTransactionalEventListenerFactory.class);

    private ApplicationContext applicationContext;

    private final Map<String, PersistTransactionalApplicationListener> listenersById = new HashMap<>();

    private final Map<Method, PersistTransactionalApplicationListener> listenersByMethod = new HashMap<>();

    public DomainTransactionalEventListenerFactory() {
        setOrder(10);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean supportsMethod(@Nonnull Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, TransactionalEventListener.class)
            && AnnotatedElementUtils.hasAnnotation(method, PersistDomainEvent.class);
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        Transactional txAnn = AnnotatedElementUtils.findMergedAnnotation(method, Transactional.class);
        if (txAnn != null) {
            Propagation propagation = txAnn.propagation();
            if (propagation != Propagation.REQUIRES_NEW && propagation != Propagation.NOT_SUPPORTED) {
                throw new IllegalStateException("@TransactionalEventListener method must not be annotated with " +
                    "@Transactional unless when declared as REQUIRES_NEW or NOT_SUPPORTED: " + method);
            }
        }

        PersistTransactionalApplicationListener p = new PersistTransactionalApplicationListener(beanName, type, method, getDomainEventRepository(method));
        listenersById.put(p.getListenerId(), p);
        listenersByMethod.put(method, p);
        return p;
    }

    @Nullable
    public PersistTransactionalApplicationListener getListener(String listenerId) {
        return listenersById.get(listenerId);
    }

    @Nullable
    public PersistTransactionalApplicationListener getListener(Method method) {
        return listenersByMethod.get(method);
    }

    @Nullable
    public DomainEventRepository getDomainEventRepository(Method method) {
        PersistDomainEvent persistDomainEvent = AnnotatedElementUtils.findMergedAnnotation(method, PersistDomainEvent.class);
        String domainEventRepositoryBeanName = Objects.requireNonNull(persistDomainEvent).repository();
        if (StringUtils.hasText(domainEventRepositoryBeanName)) {
            return applicationContext.getBean(domainEventRepositoryBeanName, DomainEventRepository.class);
        }
        try {
            return applicationContext.getBean(DomainEventRepository.class);
        } catch (Exception e) {
            log.warn("No DomainEventRepository bean is found! Domain event persistence is disabled!");
        }
        return null;
    }
}
