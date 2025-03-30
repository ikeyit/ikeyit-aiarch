package com.ikeyit.foo.interfaces.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FooJobApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooJobApp.class);
        springApplication.run(args);
    }
}
