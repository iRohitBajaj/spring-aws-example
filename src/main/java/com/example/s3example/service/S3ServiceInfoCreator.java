package com.example.s3example.service;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

public class S3ServiceInfoCreator extends CloudFoundryServiceInfoCreator<S3ServiceInfo> {
    /**
     * Default Constructor.
     * The default behavior of the constructor is to match any Cloud Foundry service with a
     * tag of "s3"
     */
    public S3ServiceInfoCreator() {
        super(new Tags("s3"));
    }

    /**
     * Parses Cloud Foundry serviceData Map and returns a normalized S3ServiceInfo instance
     * to be used by Spring Cloud service connector creators.  This Cloud Foundry serviceData
     * Map is created based on the VCAP_SERVICES environment variable.
     *
     * @param serviceData a java.util.Map containing the following required fields:  name,
     *                    accessKey, secretKey; and the following optional fields:  bucket &
     *                    endpoint.
     * @return {@link S3ServiceInfo S3ServiceInfo}
     */
    @Override
    public S3ServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        @SuppressWarnings("unchecked")
        Map<String, String> credentials = (Map<String, String>) serviceData.get("credentials");

        String id = (String) serviceData.get("name");
        String accessKey = credentials.get("accessKey");
        String secretKey = credentials.get("secretKey");
        String bucket = credentials.get("bucket");
        String endpoint = credentials.get("endpoint");

        return new S3ServiceInfo(id, accessKey, secretKey, endpoint, bucket);
    }
}
