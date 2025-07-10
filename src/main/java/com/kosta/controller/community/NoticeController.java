package com.kosta.controller.community;

import com.kosta.domain.community.Notice;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.NoticeDTO;
import com.kosta.service.community.NoticeService;
import com.kosta.util.S3PresignedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
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

    // 페이징된 공지사항 목록 조회
    @GetMapping("/page")
    public PageResponseDTO<NoticeDTO> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "noticeCreateDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String searchType) {

        log.info("Notice 페이징 목록 조회 요청 - page: {}, size: {}, sortBy: {}, sortDirection: {}, searchKeyword: {}, searchType: {}",
                page, size, sortBy, sortDirection, searchKeyword, searchType);

        // 정렬 방향 설정
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // 검색 조건에 따른 페이징 처리
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            return noticeService.searchNoticeWithPaging(searchKeyword, searchType, pageable);
        } else {
            return noticeService.getNoticeListWithPaging(pageable);
        }
    }


    // 전체 게시글 카운트
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getTotalNoticeCount() {
        try {
            long totalCount = noticeService.getTotalNoticeCount();
            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", totalCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 검색된 게시글 카운트
    @GetMapping("/search/count")
    public ResponseEntity<Map<String, Object>> getSearchNoticeCount(
            @RequestParam String searchType,
            @RequestParam String keyword) {
        try {
            long searchCount = noticeService.getSearchNoticeCount(searchType, keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("searchCount", searchCount);
            response.put("searchType", searchType);
            response.put("keyword", keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 검색 (페이징)
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchNotices(
            @RequestParam String searchType,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Notice> notices = noticeService.searchNotices(searchType, keyword, pageable);
            long searchCount = noticeService.getSearchNoticeCount(searchType, keyword);

            Map<String, Object> response = new HashMap<>();
            response.put("notices", notices.getContent());
            response.put("searchCount", searchCount);
            response.put("totalPages", notices.getTotalPages());
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("searchType", searchType);
            response.put("keyword", keyword);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // CRUD
    @GetMapping("/{noticeNo}")
    public NoticeDTO detail(@PathVariable int noticeNo) {
        log.info("Notice 선택된 상태 조회 요청");
        return noticeService.getDetailNotice(noticeNo);
    }

    @PostMapping("")
    public NoticeDTO insert(@RequestBody NoticeDTO dto,  Authentication authentication ,@AuthenticationPrincipal String memberId){
        log.info("=== Controller 디버깅 ===");
        log.info("JWT Authentication name: {}", authentication.getName());
        log.info("JWT Authentication principal: {}", authentication.getPrincipal());
        log.info("프론트엔드에서 받은 NoticeDTO.memberId: {}", dto.getMemberId());

        System.out.println("memberId 쳌! "+memberId);
        System.out.println("Controller에서 받은 dto: " + dto);
        System.out.println("Controller에서 받은 dto 모든 요소: " + dto.toString());
        System.out.println("Controller에서 받은 imageUrls: " + dto.getImageUrls());

        // 현재 권한 목록 확인
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        authorities.forEach(auth -> log.info("현재 권한: {}", auth.getAuthority()));


        // JWT에서 실제 사용자 ID 사용
        String actualUserId = authentication.getName();
        log.info("실제 사용할 사용자 ID: {}", actualUserId);

        // DTO에 올바른 사용자 ID 설정
        dto.setMemberId(actualUserId);
        log.info("설정된 NoticeDTO.memberId: {}", dto.getMemberId());

//        NoticeDTO result = noticeService.insertNotice(dto);
//        return result;
//
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



    // 조회수 증가
    @PatchMapping("/{noticeNo}/views")
    public ResponseEntity<Void> incrementViewCount(@PathVariable int noticeNo){
        noticeService.incrementViewCount(noticeNo);
        return ResponseEntity.ok().build();
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
