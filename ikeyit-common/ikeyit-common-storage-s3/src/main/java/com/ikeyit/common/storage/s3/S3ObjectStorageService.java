package com.ikeyit.common.storage.s3;

import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.common.storage.PresignResult;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URI;

public class S3ObjectStorageService implements ObjectStorageService {

    private final S3ObjectStorageProperties s3ObjectStorageProperties;

    private final S3Presigner presigner;

    public S3ObjectStorageService(S3ObjectStorageProperties s3ObjectStorageProperties) {
        this.s3ObjectStorageProperties = s3ObjectStorageProperties;
        this.presigner = S3Presigner.builder().region(Region.of(s3ObjectStorageProperties.getRegion()))
            .endpointOverride(URI.create(s3ObjectStorageProperties.getEndpoint()))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3ObjectStorageProperties.getAccessKey(), s3ObjectStorageProperties.getSecretKey())))
            .build();
    }

    @Override
    public PresignResult presign(String key) {
        String actualBucket = resolveBlobBucket();
        String actualKey = resolveBlobKey(key);
        PresignedPutObjectRequest presignedPutObjectRequest = presigner
            .presignPutObject(b -> b.putObjectRequest(builder -> builder.bucket(actualBucket).key(actualKey))
                .signatureDuration(s3ObjectStorageProperties.getSignatureDuration()));
        String url = presignedPutObjectRequest.url().toString();
        return new PresignResult(url, actualKey, getCdnUrl(actualKey));
    }

    @Override
    public String getObjectKey(String url) {
        if (url == null || url.isEmpty())
            return "";
        URI uri = URI.create(url);
        return uri.getPath().substring(1);
    }

    @Override
    public String getCdnUrl(String url) {
        if (url == null || url.isEmpty())
            return "";
        // check if the url is absolute url
        if (url.startsWith("http://") || url.startsWith("https://"))
            return url;    
        return s3ObjectStorageProperties.getCdnUrl() + url;
    }

    // All stores and tenants share the same bucket. In the future, it can be sharded by tenant or store
    protected String resolveBlobBucket() {
        return s3ObjectStorageProperties.getBucket();
    }

    protected String resolveBlobKey(String key) {
        return key;
    }
}
