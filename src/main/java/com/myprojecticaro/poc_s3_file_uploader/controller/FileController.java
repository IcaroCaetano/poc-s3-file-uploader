import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

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
}
