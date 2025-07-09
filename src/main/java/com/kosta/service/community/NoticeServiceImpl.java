package com.kosta.service.community;

import com.kosta.domain.community.Notice;
import com.kosta.domain.community.NoticeImage;
import com.kosta.domain.member.Member;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.NoticeDTO;
import com.kosta.mapper.community.NoticeMapper;
import com.kosta.repository.community.NoticeRepository;
import com.kosta.repository.member.MemberRepository;
import com.kosta.util.FileNameUtils;
import com.kosta.util.HtmlSanitizer;
import com.kosta.util.S3PresignedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kosta.mapper.community.NoticeMapper.toDTO;
import static com.kosta.mapper.community.NoticeMapper.toEntity;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {
    private final S3PresignedService s3Service;

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public List<NoticeDTO> getAllNotice (){
        return noticeRepository.findByIsVisible("Y").stream()
                .map(NoticeMapper::toDTO)
                .collect(Collectors.toList());
    }


    public NoticeDTO getDetailNotice(int noticeNo){
        return noticeRepository.findById(noticeNo)
                .map(NoticeMapper::toDTO)
                .orElse(null);
    }

    public NoticeDTO insertNotice(NoticeDTO dto){
        System.out.println("=== insertNotice 시작 ===");
        System.out.println("dto.getImageUrls(): " + dto.getImageUrls());

        // Member 영속 객체 조회
        Member member = memberRepository.findByMemberId(dto.getMemberId());
        if(member == null){
            throw new RuntimeException("존재하지 않는 회원 입니다!");
        }

        Notice notice = NoticeMapper.toEntity(dto);
        notice.setMember(member);
        notice.setNoticeContent(HtmlSanitizer.extendedSanitize(notice.getNoticeContent()));
        notice.setNoticeCreateDate(LocalDateTime.now());
        notice.setIsDeleted("N");
        notice.setIsVisible("Y");

        // 이미지 개수 계산
        int imageCount = (dto.getImageUrls() != null) ? dto.getImageUrls().size() : 0;
        notice.setNoticeImageNumber(imageCount);

        System.out.println("=== 이미지 처리 전 ===");
        System.out.println("dto.getImageUrls() != null: " + (dto.getImageUrls() != null));

        // 이미지 처리
        if(dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()){
            System.out.println("이미지 URL 개수: " + dto.getImageUrls().size());
            for(String url : dto.getImageUrls()){
                NoticeImage noticeImage = NoticeImage.builder()
                        .notice(notice)
                        .fileUrl(url)
                        .originalFileName(FileNameUtils.extractFileName(url))
                        .fileType(FileNameUtils.extractFileType(url))
                        .uploadTime(LocalDateTime.now())
                        .build();

                // 양방향 관계 유지
                notice.getNoticeImages().add(noticeImage);
            }
            System.out.println("Notice에 추가된 이미지 개수: " + notice.getNoticeImages().size());
        } else {
            System.out.println("=== 이미지 처리 건너뜀 ===");
        }

        Notice savedNotice = noticeRepository.save(notice);
        return  NoticeMapper.toDTO(savedNotice);
    }

    public NoticeDTO updateNotice(NoticeDTO dto){
        System.out.println("=== updateNotice 시작 ===");
        System.out.println("수정할 공지사항 번호: " + dto.getNoticeNo());
        System.out.println("dto.getImageUrls(): " + dto.getImageUrls());

        // 디버깅용 로그 추가
        System.out.println("dto.getMemberId(): " + dto.getMemberId());

        // 기존 공지사항 조회
        Notice existingNotice = noticeRepository.findById(dto.getNoticeNo())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항입니다!"));

        // 삭제된 공지사항인지 확인
        if("Y".equals(existingNotice.getIsDeleted())){
            throw new RuntimeException("삭제된 공지사항은 수정할 수 없습니다!");
        }

        // 기존 작성자 정보 유지
        // 수정 시에는 작성자를 변경하지 않고 기존 Member 객체를 그대로 사용
        Member existingMember = existingNotice.getMember();
        if(existingMember == null){
            throw new RuntimeException("공지사항의 작성자 정보가 없습니다!");
        }

        // 기존 공지사항 정보 업데이트
        existingNotice.setNoticeTitle(dto.getNoticeTitle());
        existingNotice.setNoticeContent(HtmlSanitizer.extendedSanitize(dto.getNoticeContent()));
        existingNotice.setNoticeEditDate(LocalDateTime.now());

        // 기존 이미지 삭제 (cascade 설정에 따라 자동 삭제될 수도 있음)
        existingNotice.getNoticeImages().clear();

        // 새로운 이미지 개수 계산 및 설정
        int imageCount = (dto.getImageUrls() != null) ? dto.getImageUrls().size() : 0;
        existingNotice.setNoticeImageNumber(imageCount);

        System.out.println("=== 이미지 처리 전 ===");
        System.out.println("새로운 이미지 개수: " + imageCount);

        // 새로운 이미지 처리
        if(dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()){
            System.out.println("이미지 URL 개수: " + dto.getImageUrls().size());
            for(String url : dto.getImageUrls()){
                NoticeImage noticeImage = NoticeImage.builder()
                        .notice(existingNotice)
                        .fileUrl(url)
                        .originalFileName(FileNameUtils.extractFileName(url))
                        .fileType(FileNameUtils.extractFileType(url))
                        .uploadTime(LocalDateTime.now())
                        .build();

                existingNotice.getNoticeImages().add(noticeImage);
            }
            System.out.println("Notice에 추가된 이미지 개수: " + existingNotice.getNoticeImages().size());
        } else {
            System.out.println("=== 이미지 처리 건너뜀 ===");
        }

        System.out.println("=== Notice 수정 저장 전 ===");
        Notice updatedNotice = noticeRepository.save(existingNotice);
        System.out.println("=== Notice 수정 저장 후 ===");

        return NoticeMapper.toDTO(updatedNotice);
    }


    public NoticeDTO deleteNotice(int noticeNo){
        Notice notice = noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new RuntimeException("해당하는 게시글을 찾지 못했습니다."));

        // 이미지 URL 추출 (삭제 전에 저장)
        List<String> imageUrls = extractImageUrls(notice.getNoticeContent());

        // 소프트 삭제 처리
        notice.setIsVisible("N");
        notice.setNoticeDeleteDate(LocalDateTime.now());

        // 변경사항 저장
        Notice savedNotice = noticeRepository.save(notice);

        // S3에서 이미지 삭제 (비동기로 처리하거나 별도 스케줄러로 처리 가능)
        if (!imageUrls.isEmpty()) {
            deleteImagesFromS3Async(imageUrls, "notice");
        }

        // return convertToDTO(savedNotice);
        return toDTO(savedNotice);
    }

    // 검색
    @Override
    @Transactional(readOnly = true)
    public Page<Notice> searchNotices(String searchType, String keyword, Pageable pageable) {
        switch (searchType.toLowerCase()) {
            case "title":
                return searchByTitle(keyword, pageable);
            case "content":
                return searchByContent(keyword, pageable);
            case "all":
            case "title_content":
                return searchByTitleOrContent(keyword, pageable);
            default:
                return noticeRepository.findByIsVisibleOrderByNoticeCreateDateDesc("Y", pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notice> searchByTitle(String title, Pageable pageable) {
        return noticeRepository.findByIsVisibleAndNoticeTitleContainingIgnoreCase("Y", title, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notice> searchByContent(String content, Pageable pageable) {
        return noticeRepository.findByIsVisibleAndNoticeContentContainingIgnoreCase("Y", content, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notice> searchByTitleOrContent(String keyword, Pageable pageable) {
        return noticeRepository.findByIsVisibleAndNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase(
                "Y", keyword, keyword, pageable);
    }

    // ========== 카운트 관련 메서드 ==========
    @Override
    @Transactional(readOnly = true)
    public int getTotalNoticeCount() {
        return noticeRepository.countByIsVisible("Y");
    }

    @Override
    @Transactional(readOnly = true)
    public int getSearchNoticeCount(String searchType, String keyword) {
        switch (searchType.toLowerCase()) {
            case "title":
                return noticeRepository.countByIsVisibleAndNoticeTitleContainingIgnoreCase("Y", keyword);
            case "content":
                return noticeRepository.countByIsVisibleAndNoticeContentContainingIgnoreCase("Y", keyword);
            case "all":
            case "title_content":
                return noticeRepository.countByIsVisibleAndNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase("Y", keyword, keyword);
            default:
                return 0;
        }
    }



    // 페이징된 목록 조회
    public PageResponseDTO<NoticeDTO> getNoticeListWithPaging(Pageable pageable){
        log.info("페이징된 공지사항 목록 조회 - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Notice> noticePage = noticeRepository.findByIsVisibleOrderByNoticeCreateDateDesc("Y", pageable);

        List<NoticeDTO> noticeDTOs = noticePage.getContent().stream()
                .map(NoticeMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<NoticeDTO>builder()
                .content(noticeDTOs)
                .currentPage(noticePage.getNumber())
                .pageSize(noticePage.getSize())
                .totalElements(noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .first(noticePage.isFirst())
                .last(noticePage.isLast())
                .build();
    }


    // 검색 조건에 따른 페이징 처리
    public PageResponseDTO<NoticeDTO> searchNoticeWithPaging(String searchKeyword, String searchType, Pageable pageable){
        log.info("검색 조건으로 공지사항 조회 - keyword: {}, type: {}, page: {}, size: {}",
                searchKeyword, searchType, pageable.getPageNumber(), pageable.getPageSize());

        Page<Notice> noticePage;

        switch (searchType) {
            case "title":
                noticePage = noticeRepository.findByIsVisibleAndNoticeTitleContainingIgnoreCase("Y", searchKeyword, pageable);
                break;
            case "content":
                noticePage = noticeRepository.findByIsVisibleAndNoticeContentContainingIgnoreCase("Y", searchKeyword, pageable);
                break;
            case "all":
            default:
                noticePage = noticeRepository.findByIsVisibleAndNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase("Y", searchKeyword, searchKeyword, pageable);
                break;
        }

        List<NoticeDTO> noticeDTOs = noticePage.getContent().stream()
                .map(NoticeMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<NoticeDTO>builder()
                .content(noticeDTOs)
                .currentPage(noticePage.getNumber())
                .pageSize(noticePage.getSize())
                .totalElements(noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .first(noticePage.isFirst())
                .last(noticePage.isLast())
                .build();
    }


    @Transactional
    public void incrementViewCount(int noticeNo) {
        int updatedRows = noticeRepository.incrementViewCount(noticeNo);

        // 업데이트된 행이 없다면 게시글이 존재하지 않는 것
        if (updatedRows == 0) {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + noticeNo);
        }
    }


    // 이미지 URL 추출 메서드
    private List<String> extractImageUrls(String content) {
        List<String> imageUrls = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            return imageUrls;
        }

        // img 태그에서 src 속성 추출
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            // S3 URL인지 확인
            if (imageUrl.contains("kosta-blog.s3.ap-northeast-2.amazonaws.com")) {
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }

    // 비동기로 S3 이미지 삭제 처리
    @Async
    public void deleteImagesFromS3Async(List<String> imageUrls, String folder) {
        for (String imageUrl : imageUrls) {
            try {
                String filename = extractFilenameFromUrl(imageUrl);
                boolean deleted = s3Service.deleteFile(folder, filename);

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
        String[] parts = fileUrl.split("/");
        if (parts.length >= 2) {
            return parts[parts.length - 2] + "/" + parts[parts.length - 1];
        }
        return parts[parts.length - 1];
    }
}
