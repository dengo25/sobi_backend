package com.kosta.repository.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kosta.domain.community.Notice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    List<Notice> findByIsVisible(String isVisible);

    // 페이징 관련 메서드 추가
    Page<Notice> findByIsVisibleOrderByNoticeCreateDateDesc(String isVisible, Pageable pageable);

    // 제목으로 검색 (페이징)
    Page<Notice> findByIsVisibleAndNoticeTitleContainingIgnoreCase(String isVisible, String noticeTitle, Pageable pageable);

    // 내용으로 검색 (페이징)
    Page<Notice> findByIsVisibleAndNoticeContentContainingIgnoreCase(String isVisible, String noticeContent, Pageable pageable);

    // 제목 또는 내용으로 검색 (페이징)
    Page<Notice> findByIsVisibleAndNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase(
            String isVisible, String noticeTitle, String noticeContent, Pageable pageable);

    // 특정 사용자의 공지사항 조회 (페이징)
    @Query("SELECT n FROM Notice n WHERE n.member.memberId = :memberId AND n.isVisible = 'Y' ORDER BY n.noticeCreateDate DESC")
    Page<Notice> findByMemberIdAndVisibleWithPaging(@Param("memberId") String memberId, Pageable pageable);

    // 특정 기간 내 공지사항 조회 (페이징)
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' AND n.noticeCreateDate BETWEEN :startDate AND :endDate ORDER BY n.noticeCreateDate DESC")
    Page<Notice> findByDateRangeWithPaging(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // 조회수 순으로 정렬된 공지사항 조회 (페이징)
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' ORDER BY n.count DESC, n.noticeCreateDate DESC")
    Page<Notice> findByIsVisibleOrderByCountDesc(String isVisible, Pageable pageable);

    // 조회수 증가를 위한 쿼리 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Notice n SET n.count = n.count + 1 WHERE n.noticeNo = :noticeNo")
    int incrementViewCount(@Param("noticeNo") int noticeNo);

    // 전체 게시글 카운트 (보이는 게시글만)
    int countByIsVisible(String isVisible);

    // 제목으로 검색된 게시글 카운트
    int countByIsVisibleAndNoticeTitleContainingIgnoreCase(String isVisible, String noticeTitle);

    // 내용으로 검색된 게시글 카운트
    int countByIsVisibleAndNoticeContentContainingIgnoreCase(String isVisible, String noticeContent);

    // 제목 또는 내용으로 검색된 게시글 카운트
    int countByIsVisibleAndNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase(
            String isVisible, String noticeTitle, String noticeContent);

}



