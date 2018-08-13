package com.example.s3example;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
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

//    private final S3CloudService s3Service;
//
//    @Autowired
//    public S3Controller(S3CloudService s3CloudService) {
//        this.s3CloudService = s3CloudService;
//    }

    @GetMapping("")
    public ResponseEntity<List<S3ObjectSummary>> all() {
        return this.s3Service.listAll();
    }

    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody Person person) {
        StoredObject object =StoredObject.builder()
                .key(person.getName())
                .contents(person.toString())
                .build();
        return this.s3Service.upload(object);
    }

    @GetMapping("/{key}")
    public ResponseEntity<StoredObject> get(@PathVariable("key") String key) {
        return this.s3Service.downloadByKey(key);
    }

    @DeleteMapping("/{key}")
    public void delete(@PathVariable("key") String key) {
        this.s3Service.deleteByKey(key);
    }
}
