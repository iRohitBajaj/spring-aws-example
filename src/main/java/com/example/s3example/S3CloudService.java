package com.example.s3example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class S3CloudService {

    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resourcePatternResolver;

    @Value("${s3.bucketname}")
    private String S3bucketName;

    @Value("${s3.baseUrl}")
    private String S3baseUrl;

    @Autowired
    public S3CloudService(ResourceLoader resourceLoader, ResourcePatternResolver resourcePatternResolver) {
        this.resourceLoader = resourceLoader;
        this.resourcePatternResolver = resourcePatternResolver;
    }


    public ResponseEntity<List<StoredObject>> downloadAll() {

        List<StoredObject> allObjects = new ArrayList<>();

        String bucketUrl = this.S3baseUrl + this.S3bucketName;
        Resource[] allFileMatchingPatten;
        try {
            allFileMatchingPatten = this.resourcePatternResolver
                    .getResources(bucketUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(allObjects, HttpStatus.OK);
    }

    public ResponseEntity<String> upload(StoredObject object) {

        String objectUrl = this.S3baseUrl + this.S3bucketName +"/" + object.getKey();

        WritableResource resource = (WritableResource) resourceLoader
                .getResource(objectUrl);

        try (OutputStream outputStream = resource.getOutputStream()) {
            outputStream.write(object.getContents().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(objectUrl, HttpStatus.OK);
    }

    public ResponseEntity<StoredObject> downloadByKey(String key) {
        StoredObject downloadedObject = null;
        String objectUrl = this.S3baseUrl + this.S3bucketName +"/" + key;
        Resource resource = resourceLoader.getResource(objectUrl);
        String contents = null;
        try (InputStream inputStream = resource.getInputStream()) {
            contents = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(StoredObject.builder()
                    .bucket(this.S3bucketName)
                    .key(key)
                    .url(objectUrl)
                    .contents(contents)
                .build(), HttpStatus.OK);
    }

}
