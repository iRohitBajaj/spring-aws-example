package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.baseUrl}")
    private String S3baseUrl;

    @Value("${s3.bucketname}")
    private String bucket;


    public ResponseEntity<String> upload(StoredObject object) {

        String objectUrl = this.S3baseUrl + this.bucket +"/" + object.getKey();

        InputStream inputStream = new ByteArrayInputStream(object.toString().getBytes());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, object.getKey(), inputStream, objectMetadata);

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

        return new ResponseEntity<>(objectUrl, HttpStatus.OK);
    }

    public ResponseEntity<StoredObject> downloadByKey(String key){

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

        S3Object s3Object = amazonS3.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        String objectUrl = this.S3baseUrl + this.bucket +"/" + key;

        String contents = new BufferedReader(new InputStreamReader(objectInputStream))
                .lines().collect(Collectors.joining("\n"));

        return new ResponseEntity<>(StoredObject.builder()
                .bucket(this.bucket)
                .key(key)
                .url(objectUrl)
                .contents(contents)
                .build(), HttpStatus.OK);
    }

    public void deleteByKey(String key){
        amazonS3.deleteObject(this.bucket, key);
    }

    public ResponseEntity<List<S3ObjectSummary>> listAll() {
        ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(bucket));

        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        return new ResponseEntity<>(s3ObjectSummaries, HttpStatus.OK);
    }

}
