package com.ikeyit.common.data.spring.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Executor;

/**
 * Based on execution context, execute event listener code in the same thread or delegate to the original executor.
 */

public class DomainEventExecutor implements TaskExecutor {
    public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";

    private static final Logger log = LoggerFactory.getLogger(DomainEventExecutor.class);

    private Executor delegated;

    private BeanFactory beanFactory;

    public DomainEventExecutor() {

    }

    public DomainEventExecutor(Executor delegated) {
        this.delegated = delegated;
    }

    @Override
    public void execute(Runnable task) {
        if (ExecutionContext.forceSyncEnabled()) {
            log.debug("Run task in sync mode");
            task.run();
        } else {
            if (delegated == null) {
                delegated = getDelegatedExecutor();
            }
            delegated.execute(task);
        }
    }

    protected Executor getDelegatedExecutor() {
        if (beanFactory == null) {
            throw new IllegalStateException("BeanFactory is not set!");
        }
        try {
            return beanFactory.getBean(TaskExecutor.class);
        } catch (NoSuchBeanDefinitionException ex) {
            try {
                return beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, Executor.class);
            }
            catch (NoSuchBeanDefinitionException e) {
                log.error("The default task executor bean is not found!");
                throw e;
            }
        }
    }


//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
//    }
}
