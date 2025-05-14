package com.ikeyit.access.interfaces.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AccessJobApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(AccessJobApp.class);
        springApplication.run(args);
    }
}
