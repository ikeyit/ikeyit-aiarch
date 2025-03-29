package com.ikeyit.common.data.spring.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Executor;

/**
 * A task executor that can execute domain event listeners either synchronously or asynchronously.
 * Based on the execution context, this executor will either run the task in the same thread
 * or delegate to another executor (typically a thread pool) for asynchronous execution.
 * This allows for flexible handling of domain events based on the current context requirements.
 */
public class DomainEventExecutor implements TaskExecutor {
    /**
     * The default bean name for the task executor in Spring context.
     */
    public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(DomainEventExecutor.class);

    /**
     * The executor to delegate tasks to when running in asynchronous mode.
     */
    private Executor delegated;

    /**
     * The bean factory used to look up the delegated executor if not explicitly provided.
     */
    private BeanFactory beanFactory;

    /**
     * Default constructor.
     * When using this constructor, a delegated executor will be looked up from the bean factory.
     */
    public DomainEventExecutor() {

    }

    /**
     * Constructor with a specific delegated executor.
     *
     * @param delegated The executor to delegate tasks to when running in asynchronous mode
     */
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

    /**
     * Gets the delegated executor from the bean factory.
     * First tries to get a TaskExecutor bean, then falls back to the default executor bean name.
     *
     * @return The delegated executor to use for asynchronous task execution
     * @throws IllegalStateException if the bean factory is not set
     * @throws NoSuchBeanDefinitionException if no suitable executor bean is found
     */
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


    /**
     * Sets the bean factory for this executor.
     * This is required when using the default constructor to look up the delegated executor.
     *
     * @param beanFactory The Spring bean factory to use
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
