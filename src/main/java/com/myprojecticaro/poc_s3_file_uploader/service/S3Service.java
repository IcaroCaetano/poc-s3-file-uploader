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
    private final S3TransferManager transferManager;
    private final String bucketName = "your-bucket-name";

    public S3Service(S3Client s3Client, S3TransferManager transferManager) {
        this.s3Client = s3Client;
        this.transferManager = transferManager;
    }


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

    /**
     * Lists all files in the S3 bucket.
     *
     * @return List of filenames
     */
    public List<String> listFiles() {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

        return listRes.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    /**
     * Downloads a file from S3 by its filename.
     *
     * @param filename The key (file name) in the bucket
     * @return File content as bytes
     */
    public byte[] downloadFile(String filename) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

        try {
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file: " + filename, e);
        }
    }

     /**
     * Deletes a file from an S3 bucket.
     *
     * <p>This method sends a {@link DeleteObjectRequest} to AWS S3
     * and removes the object specified by the filename (key).
     *
     * @param filename the name of the file (S3 object key) to delete
     * @throws software.amazon.awssdk.core.exception.SdkException
     *         if the AWS SDK encounters an error during the delete operation
     */
    public void deleteFile(String filename) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

      /**
     * Uploads a large file (5GB+) using S3 multipart upload via TransferManager.
     *
     * @param file the multipart file to upload
     * @return the S3 URL of the uploaded file
     * @throws IOException if the input stream cannot be read
     */
    public String uploadLargeFile(MultipartFile file) throws IOException {

        String key = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        UploadFileRequest uploadRequest = UploadFileRequest.builder()
                .putObjectRequest(p -> p.bucket(bucketName).key(key))
                .source(file.getResource())
                .build();

        FileUpload upload = transferManager.uploadFile(uploadRequest);

        upload.completionFuture().join(); // Wait for completion

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
