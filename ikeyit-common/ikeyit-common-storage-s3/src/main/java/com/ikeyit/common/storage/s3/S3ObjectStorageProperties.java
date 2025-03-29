package com.ikeyit.common.storage.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("ikeyit.tenant.blobstore")
public class S3ObjectStorageProperties {
    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private Duration signatureDuration = Duration.ofHours(1);
    private String bucket;
    private String cdnUrl;
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Duration getSignatureDuration() {
        return signatureDuration;
    }

    public void setSignatureDuration(Duration signatureDuration) {
        this.signatureDuration = signatureDuration;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }
}
