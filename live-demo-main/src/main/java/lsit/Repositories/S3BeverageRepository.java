package lsit.Repositories;

import java.net.URI;
import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lsit.Models.Beverage;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Primary
@Repository
public class S3BeverageRepository implements IBeverageRepository {
    private static final String BUCKET = "beverage_repository";
    private static final String PREFIX = "beverage-store/beverages/";
    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    private static final String ENDPOINT_URL = "https://storage.googleapis.com";

    private final S3Client s3client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public S3BeverageRepository() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of("auto"))
                .build();
    }

    @Override
    public void add(Beverage beverage) {
        try {
            beverage.setId(UUID.randomUUID());
            String beverageJson = objectMapper.writeValueAsString(beverage);

            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + beverage.getId())
                            .build(),
                    RequestBody.fromString(beverageJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Beverage get(UUID id) {
        try {
            var objectBytes = s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + id.toString())
                            .build()
            ).readAllBytes();

            return objectMapper.readValue(objectBytes, Beverage.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void remove(UUID id) {
        s3client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(BUCKET)
                        .key(PREFIX + id.toString())
                        .build()
        );
    }

    @Override
    public void update(Beverage beverage) {
        try {
            if (get(beverage.getId()) == null) return;

            String beverageJson = objectMapper.writeValueAsString(beverage);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + beverage.getId())
                            .build(),
                    RequestBody.fromString(beverageJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Beverage> list() {
        List<Beverage> beverages = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(
                ListObjectsRequest.builder()
                        .bucket(BUCKET)
                        .prefix(PREFIX)
                        .build()
        ).contents();

        for (S3Object obj : objects) {
            try {
                UUID id = UUID.fromString(obj.key().substring(PREFIX.length()));
                Beverage beverage = get(id);
                if (beverage != null) {
                    beverages.add(beverage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return beverages;
    }
}