package com.myprojecticaro.poc_s3_file_uploader.controller;

import com.myprojecticaro.awsupload.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller responsável por gerenciar operações relacionadas a arquivos no AWS S3.
 * <p>
 * Atualmente, esta classe expõe um endpoint para upload de arquivos.
 * O arquivo é recebido via requisição HTTP e encaminhado para o {@link S3Service},
 * que realiza o upload no bucket configurado da AWS.
 * </p>
 *
 * <p>
 * Exemplo de requisição:
 * <pre>
 * POST /files/upload
 * Content-Type: multipart/form-data
 * Body: file=[arquivo]
 * </pre>
 * </p>
 *
 * <p>
 * Retorna a URL pública do arquivo armazenado no S3 ou uma mensagem de erro
 * em caso de falha durante o upload.
 * </p>
 *
 * @author Icaro
 * @version 1.0
 * @since 2025-10-13
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final S3Service s3Service;

    /**
     * Construtor que injeta a dependência {@link S3Service}.
     *
     * @param s3Service serviço responsável por realizar operações no AWS S3.
     */
    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Endpoint responsável por fazer upload de um arquivo para o AWS S3.
     *
     * <p>Recebe um arquivo via formulário multipart e retorna a URL
     * pública do arquivo armazenado.</p>
     *
     * @param file o arquivo a ser enviado para o S3.
     * @return {@link ResponseEntity} contendo a URL do arquivo em caso de sucesso
     * ou uma mensagem de erro em caso de falha.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Received upload request for file: {}", file.getOriginalFilename());
        try {
            String fileUrl = s3Service.uploadFile(file);
            logger.info("File uploaded successfully: {}", fileUrl);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            logger.error("Upload failed for file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Lists all files available in the S3 bucket.
     *
     * @return a list of filenames
     */
    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        logger.info("Listing all files in S3 bucket");
        List<String> files = s3Service.listFiles();
        return ResponseEntity.ok(files);
    }

    /**
     * Downloads a specific file from S3 by its name.
     *
     * @param filename the name of the file to download
     * @return the file content as a byte array
     */
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        logger.info("Downloading file from S3: {}", filename);
        try {
            byte[] fileBytes = s3Service.downloadFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (Exception e) {
            logger.error("Error downloading file: {}", filename, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
