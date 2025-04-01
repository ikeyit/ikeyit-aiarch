package com.ikeyit.account.interfaces.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AccountJobApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccountJobApp.class);
        springApplication.run(args);
    }
}
