package com.ikeyit.foo.interfaces.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FooConsumerApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooConsumerApp.class);
        springApplication.run(args);
    }
}
