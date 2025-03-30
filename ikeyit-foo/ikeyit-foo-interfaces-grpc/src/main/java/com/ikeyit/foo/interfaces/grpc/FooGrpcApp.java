package com.ikeyit.foo.interfaces.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FooGrpcApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooGrpcApp.class);
        springApplication.run(args);
    }
}
