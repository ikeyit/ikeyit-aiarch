package com.ikeyit.foo.interfaces.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * <pre>
 * === AI-NOTE ===
 * Name the class as FooApiApp
 * === AI-NOTE-END ===
 * <pre/>
 */
@SpringBootApplication
public class FooApiApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooApiApp.class);
        springApplication.run(args);
    }
}
