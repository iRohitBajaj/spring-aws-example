package com.example.s3example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S3exampleApplication {

	private final String TAG_NAME = "conversationid";

	public static void main(String[] args) {
		SpringApplication.run(S3exampleApplication.class, args);
	}

}
