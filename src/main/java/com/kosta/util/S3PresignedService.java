package com.kosta.util;

import com.amazonaws.services.s3.AmazonS3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3PresignedService {
//  @Autowired
//  private AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${aws.s3.region}")
  private String region;
  
  private final S3Presigner s3Presigner;
  private final S3Client s3Client; // AmazonS3 대신 S3Client 사용

  public List<PresignedUrlResponseDTO> createPresignedUrls(PresignedUrlRequestDTO requestDTO) {
    List<PresignedUrlResponseDTO> resultList = new ArrayList<>();
    
    for (String filename : requestDTO.getFilenames()) {
      
      // S3에 저장될 키 생성 (예: review/uuid_filename.jpg)
      String key = requestDTO.getFolder() + "/" + UUID.randomUUID() + "_" + filename;
      
      // S3에 대한 PutObjectRequest 구성
      PutObjectRequest objectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();
      
      // Presigned URL 요청 구성 (유효시간: 10분)
      PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .putObjectRequest(objectRequest)
          .build();
      
      // Presigned URL 생성
      URL presignedUrl = s3Presigner.presignPutObject(presignRequest).url();
      
      // 응답 DTO 구성 및 리스트에 추가
      PresignedUrlResponseDTO dto = new PresignedUrlResponseDTO(key, presignedUrl.toString());
      resultList.add(dto);
    }
    
    return resultList;
    
    //결론적으로 aws s3에 요청해서 url를 받는 게 아니라 sdk를 이용해서 url를 만든다.
  }
  
/*  [
  {
    "filename": "review/f7f8e0e4_abc.jpg",
      "presignedUrl": "https://kosta.s3.ap-northeast-2.amazonaws.com/review/xxxxx"
  },
  {
    "filename": "review/84d02928_def.png",
      "presignedUrl": "https://kosta.s3.ap-northeast-2.amazonaws.com/review/xxxxx"
  }
]*/


  public boolean deleteFile(String folder, String filename) {
    try {
      // 폴더가 포함된 전체 키 생성
      String key = filename.startsWith(folder) ? filename : folder + "/" + filename;

      // S3에서 파일 삭제( AWS SDK v2 방식 )
      DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .build();
      s3Client.deleteObject(deleteRequest);

      log.info("S3 파일 삭제 완료: {}", key);
      return true;
    } catch (Exception e) {
      log.error("S3 파일 삭제 실패: {}/{}", folder, filename, e);
      return false;
    }
  }
}