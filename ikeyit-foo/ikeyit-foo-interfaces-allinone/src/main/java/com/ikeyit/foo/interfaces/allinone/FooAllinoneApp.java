package com.ikeyit.foo.interfaces.allinone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Includes all the interfaces in one application
 */
@SpringBootApplication
public class FooAllinoneApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooAllinoneApp.class);
        springApplication.run(args);
    }
}
