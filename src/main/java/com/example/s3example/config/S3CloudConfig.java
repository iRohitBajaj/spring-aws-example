package com.example.s3example.config;

import com.example.s3example.S3Service;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class S3CloudConfig extends AbstractCloudConfig {

    @Bean
    public S3Service s3Service() {
        return connectionFactory().service(S3Service.class);
    }

}
