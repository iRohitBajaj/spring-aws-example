package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final ObjectMapper objectMapper;

    private final AmazonS3 amazonS3;

    @Value("${s3.baseUrl}")
    private String S3baseUrl;

    @Value("${s3.bucketname}")
    private String bucket;

    @Autowired
    public S3Service(ObjectMapper objectMapper, AmazonS3 amazonS3) {
        this.objectMapper = objectMapper;
        this.amazonS3 = amazonS3;
    }


    public String upload(StoredObject object) {

        String objectUrl = this.S3baseUrl + this.bucket +"/" + object.getKey();
        object.setBucket(this.bucket);
        object.setUrl(objectUrl);

        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        objectMetadata.setContentMD5(new String(org.apache.commons.codec.binary.Base64.encodeBase64(DigestUtils.md5(bytes))));

        InputStream inputStream = new ByteArrayInputStream(bytes);

        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucket, object.getKey(), inputStream, objectMetadata);

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

        return objectUrl;
    }

    public StoredObject downloadByKey(String key){

        GetObjectRequest getObjectRequest = new GetObjectRequest(this.bucket, key);
        StoredObject storedObject = null;
        S3Object s3Object = amazonS3.getObject(getObjectRequest);
        //S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        //String contents = convertStreamToString(objectInputStream);
        String objectUrl = this.S3baseUrl + this.bucket +"/" + key;
        try {
            storedObject = objectMapper.readValue(s3Object.getObjectContent(), StoredObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return storedObject;
    }

    public void deleteByKey(String key){
        amazonS3.deleteObject(this.bucket, key);
    }

    public List<S3ObjectSummary> listAll() {
        ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(bucket));

        return objectListing.getObjectSummaries();

    }

    public String convertStreamToString(S3ObjectInputStream objectInputStream){
        return new BufferedReader(new InputStreamReader(objectInputStream))
                .lines().collect(Collectors.joining("\n"));
    }
}
