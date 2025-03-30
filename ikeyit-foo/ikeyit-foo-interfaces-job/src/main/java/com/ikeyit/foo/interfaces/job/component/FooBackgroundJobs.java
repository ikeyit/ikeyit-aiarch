package com.ikeyit.foo.interfaces.job.component;

import com.ikeyit.foo.application.service.FooService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FooBackgroundJobs {
    private static final Logger log = LoggerFactory.getLogger(FooBackgroundJobs.class);

    private final FooService fooService;

    public FooBackgroundJobs(FooService fooService) {
        this.fooService = fooService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void stats() {
        var total = fooService.findAll().size();
        log.info("Total foo count: {}", total);
    }
}
