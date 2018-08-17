package com.example.s3example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoredObject<T> {

    private String bucket;
    private String key;
    private String url;
    @JsonProperty("contents")
    private T contents;
}
