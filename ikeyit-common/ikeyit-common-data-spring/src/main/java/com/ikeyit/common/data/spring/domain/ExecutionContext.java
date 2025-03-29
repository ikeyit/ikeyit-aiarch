package com.ikeyit.common.data.spring.domain;

/**
 * Thread-local context for domain event execution.
 * This class provides a way to store and retrieve execution context information
 * that is specific to the current thread, such as whether to force synchronous
 * processing and which repository to use for domain events.
 */
public class ExecutionContext {
    /**
     * Thread-local storage for the execution context.
     */
    private static final ThreadLocal<ExecutionContext> CONTEXT = new ThreadLocal<>();

    /**
     * Flag indicating whether to force synchronous processing of domain events.
     */
    private final boolean forceSync;

    /**
     * The repository to use for domain events in this context.
     */
    private final DomainEventRepository replaceRepository;

    /**
     * Private constructor to create a new execution context.
     *
     * @param forceSync Flag indicating whether to force synchronous processing
     * @param replaceRepository The repository to use for domain events
     */
    private ExecutionContext(boolean forceSync, DomainEventRepository replaceRepository) {
        this.forceSync = forceSync;
        this.replaceRepository = replaceRepository;
    }

    /**
     * Sets the execution context for the current thread.
     *
     * @param forceSync Flag indicating whether to force synchronous processing
     * @param replaceRepository The repository to use for domain events
     */
    public static void setContext(boolean forceSync, DomainEventRepository replaceRepository) {
        CONTEXT.set(new ExecutionContext(forceSync, replaceRepository));
    }

    /**
     * Checks if synchronous processing is forced in the current context.
     *
     * @return true if synchronous processing is forced, false otherwise
     */
    public static boolean forceSyncEnabled() {
        ExecutionContext context = CONTEXT.get();
        return context != null && context.forceSync;
    }

    /**
     * Gets the repository to use for domain events in the current context.
     *
     * @return The domain event repository, or null if not set
     */
    public static DomainEventRepository replaceRepository() {
        ExecutionContext context = CONTEXT.get();
        return context == null ? null : context.replaceRepository;
    }

    /**
     * Clears the execution context for the current thread.
     * This should be called when the context is no longer needed to prevent memory leaks.
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
