package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceTestConfig {

    @Bean
    S3Service s3Service(AmazonS3 amazonS3, ObjectMapper objectMapper){
        return new S3Service(objectMapper, amazonS3);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
