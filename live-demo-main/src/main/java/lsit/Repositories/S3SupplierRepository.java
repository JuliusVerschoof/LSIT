package lsit.Repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lsit.Models.Supplier;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Primary
@Repository
public class S3SupplierRepository implements ISupplierRepository {
    private static final String BUCKET = "supplier_repository";
    private static final String PREFIX = "supplier-store/suppliers/";
    private static final String ACCESS_KEY = " ";//GOOGBGGBLGJ3O4CIDWQYOARW";//GOOGBGGBLGJ3O4CIDWQYOARW
    private static final String SECRET_KEY = " ";//NVOluQ85iCEb+x6XNaF04KPvLa3u8dswmUB/Pqoa";//NVOluQ85iCEb+x6XNaF04KPvLa3u8dswmUB/Pqoa
    private static final String ENDPOINT_URL = "https://storage.googleapis.com";

    private final S3Client s3client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public S3SupplierRepository() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of("auto"))
                .build();
    }

    @Override
    public void add(Supplier supplier) {
        try {
            supplier.setId(UUID.randomUUID());
            String supplierJson = objectMapper.writeValueAsString(supplier);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + supplier.getId())
                            .build(),
                    RequestBody.fromString(supplierJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Supplier get(UUID id) {
        try {
            String key = PREFIX + id.toString();
            System.out.println("Fetching supplier with key: " + key);
    
            var objectBytes = s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(key)
                            .build()
            ).readAllBytes();
    
            return objectMapper.readValue(objectBytes, Supplier.class);
        } catch (NoSuchKeyException e) {
            System.err.println("No such key found in S3: " + PREFIX + id.toString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching supplier with ID: " + id, e);
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
    public void update(Supplier updatedSupplier) {
        try {
            Supplier existingSupplier = get(updatedSupplier.getId());
     
            if (existingSupplier == null) {
                System.err.println("Supplier not found for ID: " + updatedSupplier.getId());
                return;
            }
    
            // Merge fields
            if (updatedSupplier.getName() != null) {
                existingSupplier.setName(updatedSupplier.getName());
            }
            if (updatedSupplier.getAssortment() != null) {
                existingSupplier.setAssortment(updatedSupplier.getAssortment());
            }
    
            // Serialize and save updated supplier
            String supplierJson = objectMapper.writeValueAsString(existingSupplier);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + existingSupplier.getId())
                            .build(),
                    RequestBody.fromString(supplierJson)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Supplier> list() {
        List<Supplier> suppliers = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(
                ListObjectsRequest.builder()
                        .bucket(BUCKET)
                        .prefix(PREFIX)
                        .build()
        ).contents();

        for (S3Object obj : objects) {
            try {
                UUID id = UUID.fromString(obj.key().substring(PREFIX.length()));
                Supplier supplier = get(id);
                if (supplier != null) {
                    suppliers.add(supplier);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return suppliers;
    }
}
