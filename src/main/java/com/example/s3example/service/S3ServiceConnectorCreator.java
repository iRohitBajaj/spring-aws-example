package com.example.s3example.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.s3example.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

@Slf4j
public class S3ServiceConnectorCreator
        extends AbstractServiceConnectorCreator<S3Service, S3ServiceInfo> {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public S3Service create(S3ServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        AWSCredentials credentials = new BasicAWSCredentials(serviceInfo.getAccessKey(), serviceInfo.getSecretKey());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                serviceInfo.getEndpoint(), region);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(credentialsProvider)
                .enablePathStyleAccess()
                .build();

        log.debug("Creating connector for ECS bucket: " + serviceInfo.getBucket());
        return new S3Service(amazonS3, serviceInfo.getEndpoint(), serviceInfo.getBucket());
    }
}
