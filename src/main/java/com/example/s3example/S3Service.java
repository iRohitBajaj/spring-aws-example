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

        byte[] bytes = new byte[0];
        String objectUrl = this.S3baseUrl + this.bucket + "/" + object.getKey();
        object.setBucket(this.bucket);
        object.setUrl(objectUrl);

        try {
            bytes = objectMapper.writeValueAsBytes(object);
            try (InputStream inputStream = new ByteArrayInputStream(bytes)){
                ObjectMetadata objectMetadata = new ObjectMetadata();

                if (null != bytes) {
                    objectMetadata.setContentLength(bytes.length);
                    objectMetadata.setContentMD5(new String(org.apache.commons.codec.binary.Base64.encodeBase64(DigestUtils.md5(bytes))));

                }
                PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucket, object.getKey(), inputStream, objectMetadata);
                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
            }catch (IOException e) {
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return objectUrl;
    }

    public StoredObject downloadByKey(String key) {

        GetObjectRequest getObjectRequest = new GetObjectRequest(this.bucket, key);
        StoredObject storedObject = null;
        try {
            S3Object s3Object = amazonS3.getObject(getObjectRequest);
            if(null != s3Object && null != s3Object.getObjectContent())
            storedObject = objectMapper.readValue(s3Object.getObjectContent(), StoredObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storedObject;
    }

    public void deleteByKey(String key) {
        amazonS3.deleteObject(this.bucket, key);
    }

    public List<S3ObjectSummary> listAll() {
        ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(bucket));
        return objectListing.getObjectSummaries();

    }

}
