# 📦 POC: AWS S3 File Uploader

This is a **Spring Boot Proof of Concept (POC)** project demonstrating how to **upload files to Amazon S3** using the **AWS SDK for Java v2**.  
It provides a simple REST API endpoint that accepts file uploads and stores them in an S3 bucket.

---

## 🚀 Features

- Upload files to AWS S3
- Automatic generation of file URLs
- Configurable AWS credentials and region
- Support for **custom endpoints** (e.g., LocalStack for local testing)
- Clean architecture with service and controller layers
- Simple and extensible for production use

---

## 🧩 Project Structure


````
poc-aws-s3-upload/
├── src/main/java/com/myprojecticaro/
│ ├── controller/
│ │ └── FileController.java # Handles REST API endpoints for uploads
│ ├── service/
│ │ └── S3Service.java # Contains logic for uploading to AWS S3
│ ├── config/
│ │ └── AwsS3Config.java # Configures and provides the S3Client bean
│ └── PocS3FileUploaderApplication.java # Main Spring Boot application
└── src/main/resources/
└── application.yml # Contains AWS and application configuration

````


---

## ⚙️ Configuration

Edit the `application.yml` file with your AWS credentials and S3 details:

```yaml
aws:
  access-key: YOUR_AWS_ACCESS_KEY
  secret-key: YOUR_AWS_SECRET_KEY
  region: us-east-1
  s3:
    bucket-name: your-s3-bucket-name
    endpoint: http://localhost:4566   # Optional (useful for LocalStack)
````

## 🧠 How It Works

1. The client sends a POST request with a multipart file.

2. The FileController receives the request and delegates the upload to S3Service.

3. The S3Service uses the configured S3Client (from AwsS3Config) to upload the file.

4. A public file URL is generated and returned to the client.

## 📡 API Endpoint

POST /files/upload

Description: Uploads a file to S3.

Request:

- Content-Type: multipart/form-data

- Parameter: file (the file to upload)

Example (using curl):

````
curl -X POST http://localhost:8080/files/upload \
  -F "file=@/path/to/your/file.png"
````

Response:

````
"https://your-s3-bucket.s3.us-east-1.amazonaws.com/1697056123456_file.png"
````

## 🧰 Build & Run

Using Gradle (default)

````
./gradlew clean build
./gradlew bootRun
````

Or using Maven (if applicable)

````
mvn clean install
mvn spring-boot:run
````

Then open:
👉 http://localhost:8080/files/upload

## 🧪 Testing with Postman

1. Open Postman

2. Create a new POST request to http://localhost:8080/files/upload

3. Under Body → form-data, add a key named file of type File

4. Choose a local file and send the request

5. You’ll receive a public S3 URL if successful

## 🧱 Technologies Used

- Java 17+

- Spring Boot 3+

- AWS SDK v2 (S3Client)

- SLF4J / Logback

- Gradle or Maven

## 🧑‍💻 Author

Icaro Caetano de Figueiredo
Developer & Cloud Enthusiast ☁️
📎 GitHub Profile(https://github.com/IcaroCaetano)
