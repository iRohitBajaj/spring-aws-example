package com.example.s3example;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Person {

    private String name;
    private Double height;

}
