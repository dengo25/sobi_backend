package com.kosta.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional
public class S3Uploader {
  
  
  private final AmazonS3 amazonS3;
  
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  
  @Value("${cloud.aws.region.static}")
  private String region;
  
  public String upload(MultipartFile multipartFile, String folder) throws Exception {
    String originalFileName = multipartFile.getOriginalFilename();
    String fileName = folder + "/" + UUID.randomUUID() + "_" + originalFileName;
    
    ObjectMetadata metadata = new ObjectMetadata(); //S3 객체(파일)의 부가 정보를 담는 클래스
    metadata.setContentLength(multipartFile.getSize()); //파일의 바이트 크기를 지정
    metadata.setContentType(multipartFile.getContentType()); //파일의 MIME 타입 (ex: image/png)를 지정
    
    amazonS3.putObject(new PutObjectRequest(
        bucket, //버킷이름
        fileName,
        multipartFile.getInputStream(),
        metadata
    ).withCannedAcl(CannedAccessControlList.PublicRead));
    
    return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
  }
  
  //s3 삭제 메서드
  public void delete(String fileUrl) {
    try {
      String key = extractKeyFromUrl(fileUrl);
      amazonS3.deleteObject(bucket, key);
    } catch (Exception e) {
      System.out.println("S3Uploader.delete 실패: " + e.getMessage());
    }
  }
  
  
  /* 삭제시 쓰이는 메서드이 특정 부분 추출
      S3 퍼블릭 URL에서 key 경로만 추출
      예: https://kosta-blog.s3.ap-northeast-2.amazonaws.com/notice/abc.png → notice/abc.png
  */
  private String extractKeyFromUrl(String url) {
    String baseUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
    return url.replace(baseUrl, "");
  }


}
