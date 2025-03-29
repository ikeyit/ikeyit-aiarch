package com.ikeyit.common.storage;

public class PresignResult {
    private String url;
    private String key;
    private String cdnUrl;

    public PresignResult() {
    }

    public PresignResult(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public PresignResult(String url, String key, String cdnUrl) {
        this.url = url;
        this.key = key;
        this.cdnUrl = cdnUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }
}
