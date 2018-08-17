package com.example.s3example;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/persons")
public class S3Controller {

    private S3Service s3Service;

    @Autowired
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }


    @GetMapping("")
    public ResponseEntity<List<S3ObjectSummary>> all() {
        return new ResponseEntity<>(s3Service.listAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody Person person) {
        StoredObject object = StoredObject.builder()
                .key(person.getName())
                .contents(person)
                .build();
        return new ResponseEntity<>(s3Service.upload(object), HttpStatus.OK);
    }

    @GetMapping("/{key}")
    public ResponseEntity get(@PathVariable("key") String key) {
        return new ResponseEntity<>(s3Service.downloadByKey(key), HttpStatus.OK);
    }

    @DeleteMapping("/{key}")
    public void delete(@PathVariable("key") String key) {
        this.s3Service.deleteByKey(key);
    }
}
