package com.example.s3example.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!cloud")
@Slf4j
public class S3LocalConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${s3.baseUrl}")
    private String s3baseUrl;

    @Value("${s3.bucketname}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Service s3Service(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        //AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
          //      s3baseUrl, region);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                //.withEndpointConfiguration(endpointConfig)
                .withCredentials(credentialsProvider)
                .enablePathStyleAccess()
                .build();

        log.debug("Creating connector addressing ECS bucket for local: " + bucket);
        return new S3Service(amazonS3, s3baseUrl, bucket);
    }
}
