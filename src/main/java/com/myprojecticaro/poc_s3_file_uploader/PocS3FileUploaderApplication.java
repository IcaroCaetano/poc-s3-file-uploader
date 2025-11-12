package com.myprojecticaro.poc_s3_file_uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the AWS S3 File Uploader Proof of Concept (POC) application.
 * <p>
 * This class bootstraps the Spring Boot application, initializing
 * the context and all configured beans.
 * </p>
 *
 * <p>Features of this application include:</p>
 * <ul>
 *   <li>Uploading files to AWS S3 using the AWS SDK v2.</li>
 *   <li>Simple REST API for file upload testing.</li>
 *   <li>Extensible architecture for further cloud integrations.</li>
 * </ul>
 *
 * <p>To start the application, simply run this class. It will launch
 * an embedded server (default: Tomcat) and make the REST API available
 * on the configured port (default: <b>8080</b>).</p>
 *
 * <p>Example:</p>
 * <pre>
 *     $ ./gradlew bootRun
 * </pre>
 *
 * @author Icaro Caetano
 * @version 1.0
 * @since 2025-10
 */
@SpringBootApplication
public class PocS3FileUploaderApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command-line arguments passed during startup
     */
    public static void main(String[] args) {
        SpringApplication.run(PocS3FileUploaderApplication.class, args);
    }
}
