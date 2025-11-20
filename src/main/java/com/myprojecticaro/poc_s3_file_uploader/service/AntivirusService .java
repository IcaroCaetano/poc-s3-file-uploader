package com.myprojecticaro.poc_s3_file_uploader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Mock antivirus service used to simulate malware scanning before uploading to S3.
 * In real-world scenarios, this would call a virus scanning engine such as ClamAV, Sophos, or a Lambda function.
 */
@Service
public class AntivirusService {

    /**
     * Simulates an antivirus scan by checking for forbidden patterns in file names.
     * This is a mock implementation used only for demonstration.
     *
     * @param file the file to scan
     * @throws RuntimeException if the file is considered malicious
     */
    public void scanFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        // Mock rule: Block files containing the word "virus"
        if (filename != null && filename.toLowerCase().contains("virus")) {
            throw new RuntimeException("Malicious file detected: " + filename);
        }

        // Mock rule: Block .exe files as suspicious
        if (filename != null && filename.endsWith(".exe")) {
            throw new RuntimeException("Executable files are not allowed: " + filename);
        }

        // Mock rule: If size is 0, treat as invalid
        if (file.isEmpty()) {
            throw new RuntimeException("Empty files are not allowed.");
        }

        // Otherwise, simulate a clean file
    }
}
