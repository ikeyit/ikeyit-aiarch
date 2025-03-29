package com.ikeyit.common.data.spring.domain;

public class ExecutionContext {
    private static final ThreadLocal<ExecutionContext> CONTEXT = new ThreadLocal<>();

    private final boolean forceSync;

    private final DomainEventRepository replaceRepository;

    private ExecutionContext(boolean forceSync, DomainEventRepository replaceRepository) {
        this.forceSync = forceSync;
        this.replaceRepository = replaceRepository;
    }

    public static void setContext(boolean forceSync, DomainEventRepository replaceRepository) {
        CONTEXT.set(new ExecutionContext(forceSync, replaceRepository));
    }

    public static boolean forceSyncEnabled() {
        ExecutionContext context = CONTEXT.get();
        return context != null && context.forceSync;
    }

    public static DomainEventRepository replaceRepository() {
        ExecutionContext context = CONTEXT.get();
        return context == null ? null : context.replaceRepository;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
