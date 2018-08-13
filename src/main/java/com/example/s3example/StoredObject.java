package com.example.s3example;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StoredObject {

    private String bucket;
    private String key;
    private String url;
    private String contents;

}
