package com.myprojecticaro.poc_s3_file_uploader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service responsible for compressing multiple uploaded files into a single ZIP file.
 */
@Service
public class ZipService {

    /**
     * Compresses the provided MultipartFile array into a single in-memory ZIP file.
     *
     * @param files array of files to be zipped
     * @return byte array representing the compressed ZIP file
     * @throws IOException if an error occurs while reading the files or creating the ZIP
     */
    public byte[] zipFiles(MultipartFile[] files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (MultipartFile file : files) {
            ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(file.getBytes());
            zipOut.closeEntry();
        }

        zipOut.close();
        return baos.toByteArray();
    }
}

