package com.ikeyit.foo.interfaces.job.component;

import com.ikeyit.common.data.spring.domain.DomainEventManager;
import com.ikeyit.common.data.spring.domain.DomainTransactionalEventListenerFactory;
import com.ikeyit.common.data.spring.domain.JdbcDomainEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * === AI-NOTE ===
 * - The class is used to retry failed domain events.
 * === AI-NOTE-END ===
 * </pre>
 */
@Component
public class FooDomainEventRetrier {
    private final DomainEventManager domainEventManager;

    public FooDomainEventRetrier(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                                 @Qualifier("fooTransactionManager") PlatformTransactionManager fooTransactionManager,
                                 @Qualifier("fooDomainEventRepository") JdbcDomainEventRepository fooDomainEventRepository) {
        this.domainEventManager = new DomainEventManager(
            domainTransactionalEventListenerFactory,
            fooTransactionManager,
            fooDomainEventRepository);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void fooRetryDomainEvents() {
        domainEventManager.retryEvents(Duration.ofMinutes(1), 100);
    }
}
