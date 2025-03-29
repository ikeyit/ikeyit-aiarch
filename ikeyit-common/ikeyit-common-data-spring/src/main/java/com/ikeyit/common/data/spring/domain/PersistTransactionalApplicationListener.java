package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.transaction.event.TransactionalApplicationListenerMethodAdapter;
import org.springframework.transaction.reactive.TransactionContext;

import java.lang.reflect.Method;

/**
 * A specialized transactional event listener that provides persistence capabilities for domain events.
 * This listener extends Spring's TransactionalApplicationListenerMethodAdapter to add support for
 * persisting domain events before they are processed, ensuring reliable event handling even in case
 * of system failures. It manages the lifecycle of domain events, including their persistence and cleanup.
 */
public class PersistTransactionalApplicationListener extends TransactionalApplicationListenerMethodAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PersistTransactionalApplicationListener.class);

    /**
     * Repository for persisting and managing domain events.
     */
    private final DomainEventRepository domainEventRepository;

    /**
     * Constructs a new PersistTransactionalApplicationListener.
     *
     * @param beanName the name of the bean to invoke the listener method on
     * @param targetClass the target class that the method is declared on
     * @param method the listener method to invoke
     * @param domainEventRepository the repository for persisting domain events
     */
    public PersistTransactionalApplicationListener(String beanName, Class<?> targetClass, Method method,
                                                   DomainEventRepository domainEventRepository) {
        super(beanName, targetClass, method);
        this.domainEventRepository = domainEventRepository;

    }

    /**
     * Handles the application event by first persisting it if necessary, then delegating to the superclass.
     * If a domain event repository is available and we're in an active transaction, the event will be
     * persisted before processing.
     *
     * @param event the event to handle
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (domainEventRepository != null && inActiveTransaction(event)) {
            DomainEvent domainEvent = domainEvent(event);
            if (domainEvent != null) {
                log.debug("persist domain event. listenerId: {}，event: {}", getListenerId(), domainEvent);
                domainEvent.assignEventId();
                domainEventRepository.save(new DomainEventPublication(domainEvent, getListenerId()));
            }
        }
        super.onApplicationEvent(event);
    }

    /**
     * Marks an event as completed by removing it from persistent storage.
     * This should be called after the event has been successfully processed.
     *
     * @param event the event that has been processed
     */
    public void completeEvent(Object event) {
        completeEvent(event, this.domainEventRepository);
    }

    /**
     * Marks an event as completed using the specified domain event repository.
     * This variant allows for using a different repository than the one configured.
     *
     * @param event the event that has been processed
     * @param domainEventRepository the repository to use for event cleanup
     */
    public void completeEvent(Object event, DomainEventRepository domainEventRepository) {
        DomainEvent domainEvent = domainEvent(event);
        if (domainEventRepository != null && domainEvent != null) {
            domainEventRepository.delete(domainEvent.getEventId(), getListenerId());
            log.debug("Persisted domain event is removed. id: {}，listenerId: {}", domainEvent.getEventId(), getListenerId());
        }
    }


    /**
     * Extracts the domain event from various event wrapper types.
     * Supports both PayloadApplicationEvent and direct DomainEvent instances.
     *
     * @param event the event object to extract from
     * @return the domain event if found, null otherwise
     */
    private static DomainEvent domainEvent(Object event) {
        if (event instanceof PayloadApplicationEvent<?> payloadApplicationEvent
            && payloadApplicationEvent.getPayload() instanceof DomainEvent domainEvent) {
            return domainEvent;
        } else if (event instanceof DomainEvent domainEvent) {
            return domainEvent;
        }
        return null;
    }

    /**
     * Checks if the current context has an active transaction.
     * Supports both traditional and reactive transaction contexts.
     *
     * @param event the event being processed
     * @return true if there is an active transaction, false otherwise
     */
    private static boolean inActiveTransaction(ApplicationEvent event) {
        if (org.springframework.transaction.support.TransactionSynchronizationManager.isSynchronizationActive() &&
            org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive()) {
            return true;
        }
        else if (event.getSource() instanceof TransactionContext txContext) {
            org.springframework.transaction.reactive.TransactionSynchronizationManager rtsm =
                new org.springframework.transaction.reactive.TransactionSynchronizationManager(txContext);
            return rtsm.isSynchronizationActive() && rtsm.isActualTransactionActive();
        }
        return false;
    }
}
