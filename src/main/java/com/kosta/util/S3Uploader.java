package com.kosta.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
  
  private final S3Client s3Client;
  
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  
  @Value("${cloud.aws.region.static}")
  private String region;
  
  public String upload(MultipartFile file, String folder) throws Exception {
    String originalFileName = file.getOriginalFilename();
    String fileName = folder + "/" + UUID.randomUUID() + "_" + originalFileName;
    
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .contentType(file.getContentType())
        .acl(ObjectCannedACL.PUBLIC_READ)
        .build();
    
    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    
    return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
  }
  
  public void delete(String fileUrl) {
    try {
      String key = extractKeyFromUrl(fileUrl);
      
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();
      
      s3Client.deleteObject(deleteObjectRequest);
    } catch (Exception e) {
      System.out.println("S3Uploader.delete 실패: " + e.getMessage());
    }
  }
  
  private String extractKeyFromUrl(String url) {
    String baseUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
    return url.replace(baseUrl, "");
  }
}
