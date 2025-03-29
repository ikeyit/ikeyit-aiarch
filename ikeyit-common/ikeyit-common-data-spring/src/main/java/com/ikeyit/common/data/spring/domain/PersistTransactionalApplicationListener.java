package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.transaction.event.TransactionalApplicationListenerMethodAdapter;
import org.springframework.transaction.reactive.TransactionContext;

import java.lang.reflect.Method;

public class PersistTransactionalApplicationListener extends TransactionalApplicationListenerMethodAdapter {
    private static final Logger log = LoggerFactory.getLogger(PersistTransactionalApplicationListener.class);

    private final DomainEventRepository domainEventRepository;

    /**
     * Construct a new TransactionalApplicationListenerMethodAdapter.
     *
     * @param beanName    the name of the bean to invoke the listener method on
     * @param targetClass the target class that the method is declared on
     * @param method      the listener method to invoke
     */
    public PersistTransactionalApplicationListener(String beanName, Class<?> targetClass, Method method,
                                                   DomainEventRepository domainEventRepository) {
        super(beanName, targetClass, method);
        this.domainEventRepository = domainEventRepository;

    }

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
     * Complete the event when it's consumed successfully
     * @param event
     */
    public void completeEvent(Object event) {
        completeEvent(event, this.domainEventRepository);
    }

    public void completeEvent(Object event, DomainEventRepository domainEventRepository) {
        DomainEvent domainEvent = domainEvent(event);
        if (domainEventRepository != null && domainEvent != null) {
            domainEventRepository.delete(domainEvent.getEventId(), getListenerId());
            log.debug("Persisted domain event is removed. id: {}，listenerId: {}", domainEvent.getEventId(), getListenerId());
        }
    }


    private static DomainEvent domainEvent(Object event) {
        if (event instanceof PayloadApplicationEvent<?> payloadApplicationEvent
            && payloadApplicationEvent.getPayload() instanceof DomainEvent domainEvent) {
            return domainEvent;
        } else if (event instanceof DomainEvent domainEvent) {
            return domainEvent;
        }
        return null;
    }

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
