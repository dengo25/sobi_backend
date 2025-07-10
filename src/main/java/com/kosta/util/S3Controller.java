package com.kosta.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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

  @DeleteMapping("/delete")
  public Map<String, Object> deleteFile(@RequestBody Map<String, String> request) {
    String fileUrl = request.get("fileUrl");
    String folder = request.get("folder");

    log.info("S3 파일 삭제 요청 - URL: {}, 폴더: {}", fileUrl, folder);

    try {
      // URL에서 파일명 추출
      String filename = extractFilenameFromUrl(fileUrl);

      // S3에서 파일 삭제
      boolean deleted = s3presignedService.deleteFile(folder, filename);

      if (deleted) {
        log.info("S3 파일 삭제 성공: {}", filename);
        return Map.of(
                "success", true,
                "message", "파일이 성공적으로 삭제되었습니다.",
                "deletedFile", filename
        );
      } else {
        log.error("S3 파일 삭제 실패: {}", filename);
        return Map.of(
                "success", false,
                "message", "파일 삭제에 실패했습니다."
        );
      }
    } catch (Exception e) {
      log.error("S3 파일 삭제 중 오류 발생", e);
      return Map.of(
              "success", false,
              "message", "파일 삭제 중 오류가 발생했습니다: " + e.getMessage()
      );
    }
  }

  @DeleteMapping("/delete-multiple")
  public Map<String, Object> deleteMultipleFiles(@RequestBody Map<String, Object> request) {
    List<String> fileUrls = (List<String>) request.get("fileUrls");
    String folder = (String) request.get("folder");

    log.info("S3 다중 파일 삭제 요청 - 폴더: {}, 파일 수: {}", folder, fileUrls.size());

    try {
      int deletedCount = 0;
      int failedCount = 0;

      for (String fileUrl : fileUrls) {
        try {
          String filename = extractFilenameFromUrl(fileUrl);
          boolean deleted = s3presignedService.deleteFile(folder, filename);

          if (deleted) {
            deletedCount++;
            log.info("S3 파일 삭제 성공: {}", filename);
          } else {
            failedCount++;
            log.error("S3 파일 삭제 실패: {}", filename);
          }
        } catch (Exception e) {
          failedCount++;
          log.error("S3 파일 삭제 중 오류: {}", fileUrl, e);
        }
      }

      return Map.of(
              "success", failedCount == 0,
              "message", String.format("총 %d개 파일 중 %d개 삭제 성공, %d개 실패",
                      fileUrls.size(), deletedCount, failedCount),
              "deletedCount", deletedCount,
              "failedCount", failedCount
      );
    } catch (Exception e) {
      log.error("S3 다중 파일 삭제 중 오류 발생", e);
      return Map.of(
              "success", false,
              "message", "다중 파일 삭제 중 오류가 발생했습니다: " + e.getMessage()
      );
    }
  }

  private String extractFilenameFromUrl(String fileUrl) {
    // URL에서 파일명 추출
    // 예: https://bucket.s3.region.amazonaws.com/folder/filename.jpg -> folder/filename.jpg
    String[] parts = fileUrl.split("/");
    if (parts.length >= 2) {
      // 마지막 두 부분을 합쳐서 폴더/파일명 형태로 반환
      return parts[parts.length - 2] + "/" + parts[parts.length - 1];
    }
    return parts[parts.length - 1]; // 폴더가 없는 경우 파일명만 반환
  }
}