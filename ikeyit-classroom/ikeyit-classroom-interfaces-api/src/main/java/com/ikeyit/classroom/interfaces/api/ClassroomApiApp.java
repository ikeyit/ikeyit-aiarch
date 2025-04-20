package com.ikeyit.classroom.interfaces.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassroomApiApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(ClassroomApiApp.class);
        springApplication.run(args);
    }
}