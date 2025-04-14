package com.ikeyit.foo.application.model;

/**
 * <pre>
 * === AI-NOTE ===
 * - Name a parameters object as FooCMD for creating, updating and operating something
 * === AI-NOTE-END ===
 * </pre>
 * The command to create a foo
 */
public class CreateFooCMD {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
