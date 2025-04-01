package com.ikeyit.account.interfaces.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AccountGrpcApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccountGrpcApp.class);
        springApplication.run(args);
    }
}
