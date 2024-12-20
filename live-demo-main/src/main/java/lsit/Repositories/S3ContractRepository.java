package lsit.Repositories;

import java.net.URI;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lsit.Models.Contract;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.time.LocalDate;


@Primary
@Repository
public class S3ContractRepository implements IContractRepository {
    private static final String BUCKET = "contract_repository";
    private static final String PREFIX = "contract-store/contracts/";
    private static final String ACCESS_KEY = " ";
    private static final String SECRET_KEY = " ";
    private static final String ENDPOINT_URL = "https://storage.googleapis.com";

    private final S3Client s3client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public S3ContractRepository() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of("auto"))
                .build();
    }

    @Override
    public void add(Contract contract) {
        try {
            contract.setId(UUID.randomUUID());
            String contractJson = objectMapper.writeValueAsString(contract);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + contract.getId())
                            .build(),
                    RequestBody.fromString(contractJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contract get(UUID id) {
        try {
            var objectBytes = s3client.getObject(
                    GetObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + id.toString())
                            .build()
            ).readAllBytes();

            return objectMapper.readValue(objectBytes, Contract.class);
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
    public void update(Contract updatedContract) {
        try {
            if (get(updatedContract.getId()) == null) return;

            String contractJson = objectMapper.writeValueAsString(updatedContract);
            s3client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(PREFIX + updatedContract.getId())
                            .build(),
                    RequestBody.fromString(contractJson)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contract> list() {
        List<Contract> contracts = new ArrayList<>();
        List<S3Object> objects = s3client.listObjects(
                ListObjectsRequest.builder()
                        .bucket(BUCKET)
                        .prefix(PREFIX)
                        .build()
        ).contents();

        for (S3Object obj : objects) {
            try {
                UUID id = UUID.fromString(obj.key().substring(PREFIX.length()));
                Contract contract = get(id);
                if (contract != null) {
                    contracts.add(contract);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return contracts;
    }

    @Override
    public boolean check(Contract contract) {
        String startDateString = contract.getStartDate();
        String endDateString = contract.getEndDate();
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);
        LocalDate currentDate = LocalDate.now();

        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }
}
