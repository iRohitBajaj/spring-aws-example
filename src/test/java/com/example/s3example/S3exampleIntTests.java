package com.example.s3example;

import com.amazonaws.services.s3.AmazonS3;
import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class S3exampleIntTests {

	@Autowired
	S3Service s3Service;

	@Autowired
	AmazonS3 amazonS3;

	StoredObject storedObject;

	S3Mock api;

	@Before
	public void setUp(){
		api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
		api.start();
		Person person = Person.builder()
				.height(5.0)
				.name("Test")
				.build();
		storedObject = StoredObject.builder()
				.contents(person.toString())
				.key("Test")
				.build();

		// create bucket
		amazonS3.createBucket("report-resource");
	}

	@Test
	public void uploadFile() {
		String objectUrl = s3Service.upload(storedObject);
		Assertions.assertThat(objectUrl).isEqualTo("s3://report-resource/Test");
	}

	@Test
	public void downloadFile() {
		StoredObject object = s3Service.downloadByKey("Test");
		Assertions.assertThat(object.getKey()).isEqualTo(storedObject.getKey());
		Assertions.assertThat(object.getContents()).isEqualTo(storedObject.getContents());
	}

	@After
	public void teardown(){
		api.stop();
	}

}
