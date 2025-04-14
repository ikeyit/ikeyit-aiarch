package com.ikeyit.foo.interfaces.allinone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * All-in-one application to start all interfaces
 */
@SpringBootApplication
public class FooAllinoneApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooAllinoneApp.class);
        springApplication.run(args);
    }
}
