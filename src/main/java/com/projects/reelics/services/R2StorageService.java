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
    private final String publicBaseUrl;

    public R2StorageService() {
        this.bucket = System.getenv("R2_BUCKET_NAME");
        this.endpoint = System.getenv("R2_ENDPOINT");
        this.publicBaseUrl = System.getenv("R2_PUBLIC_BASE_URL");

        AwsBasicCredentials creds = AwsBasicCredentials.create(
                System.getenv("R2_ACCESS_KEY_ID"),
                System.getenv("R2_SECRET_ACCESS_KEY")
        );

        this.s3 = S3Client.builder()
                .endpointOverride(URI.create("https://" + endpoint))
                .region(Region.US_EAST_1) // Dummy region, R2 ignores it
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

        String baseUrl = publicBaseUrl.endsWith("/")
                ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                : publicBaseUrl;

        return baseUrl + "/" + key;
    }
}