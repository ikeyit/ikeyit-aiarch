package com.ikeyit.access.sdk.example;

import com.ikeyit.access.remote.RemoteAccessService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RemoteAccessService.class)
public class AccessSdkExampleApp {

    public static void main(String[] args) {
        SpringApplication.run(AccessSdkExampleApp.class, args);
    }
}
