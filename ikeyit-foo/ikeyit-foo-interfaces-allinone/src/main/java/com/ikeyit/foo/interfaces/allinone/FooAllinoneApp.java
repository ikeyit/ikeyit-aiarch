package com.ikeyit.foo.interfaces.allinone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FooAllinoneApp {

    public static void main(String... args) {
        SpringApplication springApplication = new SpringApplication(FooAllinoneApp.class);
        springApplication.run(args);
    }
}
