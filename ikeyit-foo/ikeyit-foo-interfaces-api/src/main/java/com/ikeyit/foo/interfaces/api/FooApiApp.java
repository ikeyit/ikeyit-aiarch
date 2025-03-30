package com.ikeyit.foo.interfaces.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FooApiApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooApiApp.class);
        springApplication.run(args);
    }
}
