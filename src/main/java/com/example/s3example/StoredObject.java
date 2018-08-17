package com.example.s3example;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoredObject {

    private String bucket;
    private String key;
    private String url;
    private String contents;

}
