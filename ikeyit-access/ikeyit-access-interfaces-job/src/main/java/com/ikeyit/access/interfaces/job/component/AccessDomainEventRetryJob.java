package com.ikeyit.access.interfaces.job.component;

import com.ikeyit.common.data.spring.domain.MultiDomainEventManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class AccessDomainEventRetryJob {
    private final MultiDomainEventManager multiDomainEventManager;

    public AccessDomainEventRetryJob(@Qualifier("accessMultiDomainEventManager")
                                    MultiDomainEventManager multiDomainEventManager) {
        this.multiDomainEventManager = multiDomainEventManager;
    }


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void domainEventRetry() {
        multiDomainEventManager.retryEvents(Duration.ofMinutes(1), 100);
    }
}
