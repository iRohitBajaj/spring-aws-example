package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ServiceTestConfig.class}, webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class S3ServiceTests {

    @Autowired
    S3Service s3Service;

    @MockBean
    AmazonS3 amazonS3;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    S3Object s3Object;

    @Captor
    ArgumentCaptor<PutObjectRequest> putObjectCaptor;

    @Captor
    ArgumentCaptor<GetObjectRequest> getObjectCaptor;

    StoredObject storedObject;

    @Before
    public void setUp(){
        Person person = Person.builder()
                .height(5.0)
                .name("Test")
                .build();
        storedObject = StoredObject.builder()
                .contents(person)
                .key("Test")
                .build();
        when(amazonS3.getObject(any())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(mock(S3ObjectInputStream.class));
    }

    @Test
    public void shouldUploadObjectToBucket(){
        s3Service.upload(storedObject);
        verify(amazonS3, Mockito.times(1)).putObject(putObjectCaptor.capture());
        Assertions.assertThat(putObjectCaptor.getValue().getBucketName()).isEqualTo("report-resource");
        Assertions.assertThat(putObjectCaptor.getValue().getKey()).isEqualTo("Test");
        String uploadedObject = new BufferedReader(new InputStreamReader(putObjectCaptor.getValue().getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        Assertions.assertThat(uploadedObject).contains("5.0");
    }

    @Test
    public void shouldDownloadObjectFromBucketGivenAnObjectKey(){
        s3Service.downloadByKey("Test");
        verify(amazonS3, Mockito.times(1)).getObject(getObjectCaptor.capture());
        Assertions.assertThat(getObjectCaptor.getValue().getBucketName()).isEqualTo("report-resource");
        Assertions.assertThat(getObjectCaptor.getValue().getKey()).isEqualTo("Test");

    }
}
