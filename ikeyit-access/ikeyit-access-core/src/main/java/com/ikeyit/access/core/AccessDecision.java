package com.ikeyit.access.core;

public class AccessDecision {

    private final boolean granted;

    public AccessDecision(boolean granted) {
        this.granted = granted;
    }

    public boolean isGranted() {
        return this.granted;
    }
}
