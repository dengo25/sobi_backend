package com.kosta.util;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {
  
  private final S3PresignedService s3presignedService;
  
  // 이미지 업로드용 presigned URL 발급
  @PostMapping("/presigned")
  public List<PresignedUrlResponseDTO> getPresignedUrls(@RequestBody PresignedUrlRequestDTO requestDTO) {
    return s3presignedService.createPresignedUrls(requestDTO);
  }
}