package com.ikeyit.access.infrastructure.config;

import com.ikeyit.common.data.spring.domain.DomainEventExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration(proxyBeanMethods = false)
@EnableAsync
public class AccessExecutorConfig {

    @Bean
    public TaskExecutor accessTaskExecutor(ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder) {
        ThreadPoolTaskExecutor taskExecutor = threadPoolTaskExecutorBuilder.build();
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public DomainEventExecutor accessDomainEventExecutor(@Qualifier("accessTaskExecutor") TaskExecutor taskExecutor) {
        return new DomainEventExecutor(taskExecutor);
    }
}
