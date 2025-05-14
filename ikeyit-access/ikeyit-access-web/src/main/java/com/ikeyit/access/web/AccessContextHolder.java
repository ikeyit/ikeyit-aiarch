package com.ikeyit.access.web;

public class AccessContextHolder {
    private static final ThreadLocal<AccessContext> current = new ThreadLocal<>();

    public static void clearContext() {
        current.remove();
    }

    public static AccessContext getContext() {
        return current.get();
    }

    public static AccessContext getContextOrThrow() {
        AccessContext accessContext = current.get();
        if (accessContext == null) {
            throw new IllegalArgumentException("Access context is not found!");
        }
        return accessContext;
    }

    public static void setContext(AccessContext context) {
        current.set(context);
    }
}
