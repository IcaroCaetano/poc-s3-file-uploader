package com.myprojecticaro.poc_s3_file_uploader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Configuration class responsible for initializing and providing an AWS S3 client.
 * <p>
 * This class reads the AWS credentials, region, and (optionally) a custom endpoint
 * from the application's configuration properties (e.g., {@code application.yml}).
 * It builds and exposes a singleton {@link S3Client} bean that can be injected
 * into other components such as services for interacting with Amazon S3.
 * </p>
 *
 * <p>
 * Example configuration in <b>application.yml</b>:
 * <pre>
 * aws:
 *   access-key: YOUR_ACCESS_KEY
 *   secret-key: YOUR_SECRET_KEY
 *   region: us-east-1
 *   s3:
 *     bucket-name: my-bucket
 *     endpoint: http://localhost:4566   # Optional (for LocalStack)
 * </pre>
 * </p>
 *
 * <p>
 * If an endpoint is specified (e.g., when using LocalStack), the client will connect
 * to that endpoint instead of the default AWS S3 endpoint.
 * </p>
 *
 * @author Icaro
 * @version 1.0
 * @since 2025-10-13
 */
@Configuration
public class AwsS3Config {

    /** AWS access key ID used for authentication. */
    @Value("${aws.access-key}")
    private String accessKey;

    /** AWS secret access key used for authentication. */
    @Value("${aws.secret-key}")
    private String secretKey;

    /** AWS region where the S3 bucket is hosted. */
    @Value("${aws.region}")
    private String region;

    /**
     * Optional custom S3 endpoint (e.g., LocalStack).
     * If empty, the default AWS S3 endpoint will be used.
     */
    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    /**
     * Creates and configures an {@link S3Client} bean for interacting with AWS S3.
     *
     * <p>The client uses static credentials and the specified AWS region.
     * If a custom endpoint is provided (e.g., for testing), it overrides
     * the default AWS endpoint.</p>
     *
     * @return a configured instance of {@link S3Client}
     */
    @Bean
    public S3Client s3Client() {
        S3Client.Builder builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                );

        if (!endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
