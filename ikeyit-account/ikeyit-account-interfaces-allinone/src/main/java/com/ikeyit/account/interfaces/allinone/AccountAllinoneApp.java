package com.ikeyit.account.interfaces.allinone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Includes all the interfaces in one application
 */
@SpringBootApplication
public class AccountAllinoneApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccountAllinoneApp.class);
        springApplication.run(args);
    }
}
