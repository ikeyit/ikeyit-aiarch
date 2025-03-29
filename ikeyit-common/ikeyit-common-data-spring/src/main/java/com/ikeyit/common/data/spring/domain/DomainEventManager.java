package com.ikeyit.common.data.spring.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage the domain events, specially for failed events.
 */
public class DomainEventManager implements DomainEventRetryFilter {

    private static final Logger log = LoggerFactory.getLogger(DomainEventManager.class);
    private final DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory;
    private final PlatformTransactionManager platformTransactionManager;
    private final DomainEventRepository domainEventRepository;
    private final LinkedList<DomainEventRetryFilter> eventRetryFilters = new LinkedList<>();

    public DomainEventManager(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                              PlatformTransactionManager platformTransactionManager,
                              DomainEventRepository domainEventRepository) {
        this(domainTransactionalEventListenerFactory, platformTransactionManager, domainEventRepository, null);
    }

    public DomainEventManager(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                              PlatformTransactionManager platformTransactionManager,
                              DomainEventRepository domainEventRepository,
                              List<DomainEventRetryFilter> eventRetryFilters) {
        this.domainTransactionalEventListenerFactory = domainTransactionalEventListenerFactory;
        this.platformTransactionManager = platformTransactionManager;
        this.domainEventRepository = domainEventRepository;
        if (!CollectionUtils.isEmpty(eventRetryFilters)) {
            this.eventRetryFilters.addAll(eventRetryFilters);
        }
        this.eventRetryFilters.add(this);
    }

    /**
     * Retry the failed events
     * @param duration How older events to be selected
     * @param maxCount Max count to retrieve the event once
     */
    public void retryEvents(Duration duration, int maxCount) {
        retryEvents(duration, maxCount, -1);
    }

    /**
     * Retry the failed events
     * @param duration How older events to be selected
     * @param maxCount Max count to retrieve the event once
     * @param timeout The timeout to execute the retry. When the time is out, all events will not be marked as completed.
     *               Since some events is actually executed successfully.
     */
    public void retryEvents(Duration duration, int maxCount, int timeout) {
        log.info("Retry domain events...");
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setTimeout(timeout);
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager, transactionDefinition);
        transactionTemplate.execute(status -> {
            try {
                ExecutionContext.setContext(true, domainEventRepository);
                return doRetry(duration, maxCount);
            } finally {
                ExecutionContext.clear();
            }
        });

    }

    private int doRetry(Duration duration, int maxCount) {
        List<DomainEventPublication> eventPublications = domainEventRepository.findBefore(Instant.now().minus(duration), maxCount);
        int processedCount = 0;
        for (DomainEventPublication eventPublication : eventPublications) {
            PersistTransactionalApplicationListener applicationListener =
                domainTransactionalEventListenerFactory.getListener(eventPublication.getListenerId());
            if (applicationListener != null) {
                try {
                    DomainEventRetryFilterChain retryFilterChain = new DomainEventRetryFilterChain(eventRetryFilters);
                    retryFilterChain.doFilter(applicationListener, eventPublication);
                    processedCount++;
                } catch (Exception e) {
                    handleFailure(e, eventPublication);
                }
            } else {
                log.error("Listener doesn't exist! {}", eventPublication);
            }
        }

        return processedCount;
    }


    protected void handleFailure(Exception e, DomainEventPublication domainEventPublication) {
        log.error("Fail to republish event.", e);
    }

    @Override
    public void doFilter(PersistTransactionalApplicationListener listener, DomainEventPublication eventPublication, DomainEventRetryFilterChain filterChain) {
        listener.processEvent(new PayloadApplicationEvent<>(this, eventPublication.getEvent()));
        filterChain.doFilter(listener, eventPublication);
    }
}
