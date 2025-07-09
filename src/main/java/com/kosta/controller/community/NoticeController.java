package com.kosta.controller.community;

import com.kosta.domain.community.Notice;
import com.kosta.dto.community.FaqDTO;
import com.kosta.dto.community.NoticeDTO;
import com.kosta.service.community.NoticeService;
import com.kosta.util.S3PresignedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController()
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private S3PresignedService s3PresignedService;

    @GetMapping("")
    public List<NoticeDTO> list() {
        log.info("Notice 전체 목록 조회 요청");
        return noticeService.getAllNotice();
    }

    @GetMapping("/{noticeNo}")
    public NoticeDTO detail(@PathVariable int noticeNo) {
        log.info("Notice 선택된 상태 조회 요청");
        return noticeService.getDetailNotice(noticeNo);
    }

    @PostMapping("")
    public NoticeDTO insert(@RequestBody NoticeDTO dto, @AuthenticationPrincipal String memberId){
        System.out.println("Controller에서 받은 dto: " + dto);
        System.out.println("Controller에서 받은 imageUrls: " + dto.getImageUrls());

        // 현재 권한 목록 확인
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        authorities.forEach(auth -> log.info("현재 권한: {}", auth.getAuthority()));

        dto.setMemberId(memberId);
        return noticeService.insertNotice(dto);
    }

    @PutMapping("/{noticeNo}")
    public NoticeDTO update(@RequestBody NoticeDTO dto, @PathVariable("noticeNo") int noticeNo, Principal principal, @AuthenticationPrincipal String memberId){
        log.info("Notice 내용 수정 요청: noticeNo = {}, 사용자 = {}, 사용자 아이디={}", noticeNo, principal.getName(), memberId);

        // 기존 공지사항 조회 (기존 이미지 URL 확인용)
        NoticeDTO existingNotice = noticeService.getDetailNotice(noticeNo);

        dto.setNoticeNo(noticeNo);
        NoticeDTO updatedNotice = noticeService.updateNotice(dto);

        // 기존 이미지와 새 이미지 비교하여 삭제된 이미지 처리
        if (existingNotice != null && existingNotice.getImageUrls() != null) {
            List<String> oldImageUrls = existingNotice.getImageUrls();
            List<String> newImageUrls = dto.getImageUrls();

            // 삭제된 이미지 URL 찾기
            List<String> deletedImageUrls = oldImageUrls.stream()
                    .filter(url -> newImageUrls == null || !newImageUrls.contains(url))
                    .collect(Collectors.toList());

            // 삭제된 이미지들을 S3에서 제거
            if (!deletedImageUrls.isEmpty()) {
                log.info("삭제할 이미지 URL: {}", deletedImageUrls);
                deleteImagesFromS3(deletedImageUrls, "notice");
            }
        }

        return updatedNotice;
    }

    @DeleteMapping("/{noticeNo}")
    public Map<String, Object> delete(@PathVariable("noticeNo") int noticeNo, @AuthenticationPrincipal String memberId) {
        log.info("Notice 삭제 요청: noticeNo = {}, 사용자 = {}", noticeNo, memberId);

        try {
            // 삭제 전 공지사항 조회하여 관련 이미지 URL 확인
            NoticeDTO existingNotice = noticeService.getDetailNotice(noticeNo);

            // 공지사항 삭제
            NoticeDTO deletedNotice = noticeService.deleteNotice(noticeNo);

            // 관련 이미지들을 S3에서 삭제
            if (existingNotice != null && existingNotice.getImageUrls() != null && !existingNotice.getImageUrls().isEmpty()) {
                log.info("삭제할 이미지 URL: {}", existingNotice.getImageUrls());
                deleteImagesFromS3(existingNotice.getImageUrls(), "notice");
            }

            return Map.of(
                    "success", true,
                    "message", "공지사항이 성공적으로 삭제되었습니다.",
                    "deletedNotice", deletedNotice
            );
        } catch (Exception e) {
            log.error("공지사항 삭제 중 오류 발생", e);
            return Map.of(
                    "success", false,
                    "message", "공지사항 삭제 중 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }



    // S3에서 이미지 삭제하는 메서드
    private void deleteImagesFromS3(List<String> imageUrls, String folder) {
        for (String imageUrl : imageUrls) {
            try {
                // URL에서 파일명 추출
                String filename = extractFilenameFromUrl(imageUrl);

                // S3에서 파일 삭제
                boolean deleted = s3PresignedService.deleteFile(folder, filename);

                if (deleted) {
                    log.info("S3 이미지 삭제 성공: {}", filename);
                } else {
                    log.error("S3 이미지 삭제 실패: {}", filename);
                }
            } catch (Exception e) {
                log.error("S3 이미지 삭제 중 오류: {}", imageUrl, e);
            }
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
