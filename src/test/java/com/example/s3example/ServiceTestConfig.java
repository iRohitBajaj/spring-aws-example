package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class ServiceTestConfig {

    @Value("${s3.bucketname}")
    private String bucket;

    @Value("${s3.baseUrl}")
    private String s3baseUrl;

    @MockBean
    AmazonS3 amazonS3;

    @Bean
    S3Service s3Service(){
        return new S3Service(amazonS3, s3baseUrl, bucket);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesResolver() {

        return new PropertySourcesPlaceholderConfigurer();
    }
}
