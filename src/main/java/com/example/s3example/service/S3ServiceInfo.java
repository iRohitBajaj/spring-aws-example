package com.example.s3example.service;

import org.springframework.cloud.service.BaseServiceInfo;

public class S3ServiceInfo extends BaseServiceInfo {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String endpoint;

    public S3ServiceInfo(String id, String accessKey, String secretKey, String endpoint, String bucket) {
        super(id);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.endpoint = endpoint;
    }

    @ServiceProperty
    public String getAccessKey() {
        return accessKey;
    }

    @ServiceProperty
    public String getSecretKey() {
        return secretKey;
    }

    @ServiceProperty
    public String getBucket() {
        return bucket;
    }

    @ServiceProperty
    public String getEndpoint() {
        return endpoint;
    }
}
