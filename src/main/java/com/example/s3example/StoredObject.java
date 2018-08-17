package com.example.s3example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoredObject {

    private String bucket;
    private String key;
    private String url;
    private Person contents;
}
