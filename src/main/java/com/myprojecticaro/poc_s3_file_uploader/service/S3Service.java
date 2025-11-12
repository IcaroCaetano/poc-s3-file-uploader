package com.myprojecticaro.poc_s3_file_uploader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

/**
 * Service class responsible for managing file uploads to an AWS S3 bucket.
 * <p>
 * This class uses the AWS SDK v2 {@link S3Client} to upload files directly to the
 * configured S3 bucket. It automatically generates a unique key for each file by
 * prefixing the original file name with the current timestamp.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * String fileUrl = s3Service.uploadFile(multipartFile);
 * }
 * </pre>
 * </p>
 *
 * <p><strong>Configuration:</strong><br>
 * The target S3 bucket name must be specified in the application configuration:
 * <pre>
 * aws.s3.bucket-name: your-bucket-name
 * </pre>
 * </p>
 *
 * @author Icaro
 * @version 1.0
 */
@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    /**
     * Constructs an instance of {@code S3Service} with the provided {@link S3Client}.
     *
     * @param s3Client the AWS S3 client used to perform operations on S3
     */
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Uploads a file to the configured AWS S3 bucket.
     * <p>
     * The file key is generated using the current timestamp combined with the original
     * file name to ensure uniqueness. The uploaded file’s content type is also
     * preserved in the S3 metadata.
     * </p>
     *
     * @param file the {@link MultipartFile} to be uploaded
     * @return the public URL of the uploaded file
     * @throws IOException if the file cannot be read or uploaded
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String key = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, s3Client.region().id(), key);
    }
}
