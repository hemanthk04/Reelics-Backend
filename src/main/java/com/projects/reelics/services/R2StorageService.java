package com.projects.reelics.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.net.URI;

@Service
public class R2StorageService {

    private final S3Client s3;
    private final String bucket;
    private final String endpoint;

    public R2StorageService() {
        this.bucket = System.getenv("R2_BUCKET_NAME");
        this.endpoint = System.getenv("R2_ENDPOINT");

        AwsBasicCredentials creds = AwsBasicCredentials.create(
                System.getenv("R2_ACCESS_KEY_ID"),
                System.getenv("R2_SECRET_ACCESS_KEY")
        );

        this.s3 = S3Client.builder()
                .endpointOverride(URI.create("https://" + endpoint))
                .region(Region.US_EAST_1) // dummy, R2 ignores it
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .serviceConfiguration(b -> b
                        .pathStyleAccessEnabled(true)
                        .chunkedEncodingEnabled(false)
                )
                .build();

    }

    public String upload(File file, String key) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("audio/mpeg")
                .build();

        s3.putObject(req, RequestBody.fromFile(file));

        return "https://" + endpoint + "/" + bucket + "/" + key;
    }
}
