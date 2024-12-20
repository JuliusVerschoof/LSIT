package lsit.Repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lsit.Models.Brand;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Primary
@Repository
public class S3BrandRepository implements IBrandRepository {
    private static final String BUCKET = "brand_repository";
    private static final String PREFIX = "brand-store/brands/";
    private static final String ACCESS_KEY = " ";//GOOGBGGBLGJ3O4CIDWQYOARW";//GOOGBGGBLGJ3O4CIDWQYOARW
    private static final String SECRET_KEY = " ";//NVOluQ85iCEb+x6XNaF04KPvLa3u8dswmUB/Pqoa";//NVOluQ85iCEb+x6XNaF04KPvLa3u8dswmUB/Pqoa
    private static final String ENDPOINT_URL = "https://storage.googleapis.com";

    private final S3Client s3client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public S3BrandRepository() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of("auto"))
                .build();
    }

    @Override
    public void add(Brand brand) {
        try {
            brand.setId(UUID.randomUUID());
            String brandJson = objectMapper.writeValueAsString(brand);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + brand.getId())
                            .build(),
                    RequestBody.fromString(brandJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Brand get(UUID id) {
        try {
            var objectBytes = s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + id.toString())
                            .build()
            ).readAllBytes();

            return objectMapper.readValue(objectBytes, Brand.class);
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
    public void update(Brand updatedBrand) {
        try {
            if (get(updatedBrand.getId()) == null) return;

            String brandJson = objectMapper.writeValueAsString(updatedBrand);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + updatedBrand.getId())
                            .build(),
                    RequestBody.fromString(brandJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Brand> list() {
        List<Brand> brands = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(
                ListObjectsRequest.builder()
                        .bucket(BUCKET)
                        .prefix(PREFIX)
                        .build()
        ).contents();

        for (S3Object obj : objects) {
            try {
                UUID id = UUID.fromString(obj.key().substring(PREFIX.length()));
                Brand brand = get(id);
                if (brand != null) {
                    brands.add(brand);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return brands;
    }
}
