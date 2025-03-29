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

/**
 * Factory for creating domain event listeners that handle transactional events with persistence capabilities.
 * This factory extends Spring's RestrictedTransactionalEventListenerFactory to add support for persisting
 * domain events before they are processed, allowing for reliable event handling even in case of system failures.
 * It manages the lifecycle of PersistTransactionalApplicationListener instances and their associated
 * domain event repositories.
 */
public class DomainTransactionalEventListenerFactory extends RestrictedTransactionalEventListenerFactory implements ApplicationContextAware {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(DomainTransactionalEventListenerFactory.class);

    /**
     * Spring application context for accessing beans and resources.
     */
    private ApplicationContext applicationContext;

    /**
     * Map of listeners indexed by their unique identifier.
     */
    private final Map<String, PersistTransactionalApplicationListener> listenersById = new HashMap<>();

    /**
     * Map of listeners indexed by their corresponding method.
     */
    private final Map<Method, PersistTransactionalApplicationListener> listenersByMethod = new HashMap<>();

    /**
     * Constructs a new DomainTransactionalEventListenerFactory.
     * Sets the order to 10 to ensure proper execution order in the Spring container.
     */
    public DomainTransactionalEventListenerFactory() {
        setOrder(10);
    }

    /**
     * Sets the Spring application context.
     * Required by ApplicationContextAware interface.
     *
     * @param applicationContext the Spring application context
     * @throws BeansException if the application context cannot be set
     */
    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Checks if this factory supports the given method.
     * A method is supported if it has both @TransactionalEventListener and @PersistDomainEvent annotations.
     *
     * @param method the method to check
     * @return true if the method is supported, false otherwise
     */
    @Override
    public boolean supportsMethod(@Nonnull Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, TransactionalEventListener.class)
            && AnnotatedElementUtils.hasAnnotation(method, PersistDomainEvent.class);
    }

    /**
     * Creates a new PersistTransactionalApplicationListener for the given method.
     * Validates the method's transaction configuration and sets up the appropriate event repository.
     *
     * @param beanName the name of the bean containing the listener method
     * @param type the type of the bean
     * @param method the listener method
     * @return a new ApplicationListener instance
     * @throws IllegalStateException if the method has invalid transaction configuration
     */
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

    /**
     * Retrieves a listener by its unique identifier.
     *
     * @param listenerId the unique identifier of the listener
     * @return the listener instance, or null if not found
     */
    @Nullable
    public PersistTransactionalApplicationListener getListener(String listenerId) {
        return listenersById.get(listenerId);
    }

    /**
     * Retrieves a listener by its method.
     *
     * @param method the method associated with the listener
     * @return the listener instance, or null if not found
     */
    @Nullable
    public PersistTransactionalApplicationListener getListener(Method method) {
        return listenersByMethod.get(method);
    }

    /**
     * Retrieves the domain event repository for a given method.
     * First tries to get the repository specified in the @PersistDomainEvent annotation,
     * then falls back to the default repository bean if available.
     *
     * @param method the method to get the repository for
     * @return the domain event repository, or null if none is available
     */
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
