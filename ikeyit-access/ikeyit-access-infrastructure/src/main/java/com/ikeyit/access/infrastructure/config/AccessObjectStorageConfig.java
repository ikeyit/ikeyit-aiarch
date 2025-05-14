package com.ikeyit.access.infrastructure.config;

import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.common.storage.s3.S3ObjectStorageProperties;
import com.ikeyit.common.storage.s3.S3ObjectStorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccessObjectStorageConfig {

    @Bean
    @ConfigurationProperties("spring.blobstore.access")
    public S3ObjectStorageProperties access3ObjectStorageProperties() {
        return new S3ObjectStorageProperties();
    }

    @Bean
    public ObjectStorageService accessObjectStorageService(
        @Qualifier("access3ObjectStorageProperties")
        S3ObjectStorageProperties s3ObjectStorageProperties) {
        return new S3ObjectStorageService(s3ObjectStorageProperties);
    }
}
