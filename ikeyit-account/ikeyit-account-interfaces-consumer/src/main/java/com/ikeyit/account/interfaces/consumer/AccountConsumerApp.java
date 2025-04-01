package com.ikeyit.account.interfaces.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AccountConsumerApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccountConsumerApp.class);
        springApplication.run(args);
    }
}
