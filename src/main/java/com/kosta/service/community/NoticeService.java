package com.kosta.service.community;

import com.kosta.domain.community.Notice;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.NoticeDTO;
import com.kosta.service.common.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService extends GenericService<Notice, NoticeDTO> {
    // CRUD
    List<NoticeDTO> getAllNotice();
    NoticeDTO getDetailNotice(int noticeNo);
    NoticeDTO insertNotice(NoticeDTO dto);
    NoticeDTO updateNotice(NoticeDTO dto);
    NoticeDTO deleteNotice(int noticeNo);

    // 페이징 관련
   // PageResponseDTO<NoticeDTO> getNoticeListWithPaging(Pageable pageable);
    //PageResponseDTO<NoticeDTO> getNoticeListWithPaging(int page, int size);
    PageResponseDTO<NoticeDTO> getListWithPaging(Pageable pageable);
    PageResponseDTO<NoticeDTO> searchNoticeWithPaging(String searchKeyword, String searchType, Pageable pageable);

    // 조회수
    void incrementViewCount(int noticeNo);

    // 검색
    Page<Notice> searchNotices(String searchType, String keyword, Pageable pageable);
    Page<Notice> searchByTitle(String title, Pageable pageable);
    Page<Notice> searchByContent(String content, Pageable pageable);
    Page<Notice> searchByTitleOrContent(String keyword, Pageable pageable);

    // 카운트 관련 메서드
    int getTotalNoticeCount();
    int getSearchNoticeCount(String searchType, String keyword);

    // 메인 표출용 : 최신순(최근등록 순으로 상위 3개)
    List<NoticeDTO> getMainNoticesByDate();
}
