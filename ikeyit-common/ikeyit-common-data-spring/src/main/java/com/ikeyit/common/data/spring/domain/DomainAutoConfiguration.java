package com.ikeyit.common.data.spring.domain;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * Spring Boot auto-configuration class for domain-related components.
 * This configuration is activated when an Executor bean is present in the application context.
 * It is configured to run after TaskExecutionAutoConfiguration to ensure proper initialization order.
 *
 * Currently, this class serves as a placeholder for future domain-related auto-configuration.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(TaskExecutionAutoConfiguration.class)
@ConditionalOnBean(Executor.class)
public class DomainAutoConfiguration {
    // KEEP IT FOR FUTURE
}
