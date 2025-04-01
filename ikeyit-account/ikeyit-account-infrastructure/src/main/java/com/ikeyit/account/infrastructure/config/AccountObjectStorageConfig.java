package com.ikeyit.account.infrastructure.config;

import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.common.storage.s3.S3ObjectStorageProperties;
import com.ikeyit.common.storage.s3.S3ObjectStorageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountObjectStorageConfig {

    @Bean
    @ConfigurationProperties("ikeyit.account.s3")
    public S3ObjectStorageProperties s3ObjectStorageProperties() {
        return new S3ObjectStorageProperties();
    }

    @Bean
    public ObjectStorageService objectStorageService(S3ObjectStorageProperties s3ObjectStorageProperties) {
        return new S3ObjectStorageService(s3ObjectStorageProperties);
    }
}
