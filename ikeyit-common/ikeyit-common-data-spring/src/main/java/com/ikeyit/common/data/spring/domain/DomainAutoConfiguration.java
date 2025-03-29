package com.ikeyit.common.data.spring.domain;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(TaskExecutionAutoConfiguration.class)
@ConditionalOnBean(Executor.class)
public class DomainAutoConfiguration {
    // KEEP IT FOR FUTURE
}
