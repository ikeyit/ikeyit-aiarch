package com.ikeyit.common.storage;

public interface ObjectStorageService {
    PresignResult presign(String key);
    String getObjectKey(String presignUrl);
    String getCdnUrl(String url);
}
