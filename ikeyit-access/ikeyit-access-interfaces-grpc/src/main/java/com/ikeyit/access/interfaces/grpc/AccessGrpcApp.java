package com.ikeyit.access.interfaces.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AccessGrpcApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccessGrpcApp.class);
        springApplication.run(args);
    }
}
