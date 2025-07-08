package com.kosta.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kosta.domain.community.Notice;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    List<Notice> findByIsVisible(String isVisible);


    /*  확인후 사용예정
    // 특정 사용자의 공지사항 조회
    @Query("SELECT n FROM Notice n WHERE n.memberId = :memberId AND n.isVisible = 'Y' ORDER BY n.noticeCreateDate DESC")
    List<Notice> findByMemberIdAndVisible(@Param("memberId") String memberId);

    // 특정 기간 내 공지사항 조회
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' AND n.noticeCreateDate BETWEEN :startDate AND :endDate ORDER BY n.noticeCreateDate DESC")
    List<Notice> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // 제목으로 검색
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' AND n.title LIKE %:keyword% ORDER BY n.noticeCreateDate DESC")
    List<Notice> findByTitleContainingAndVisible(@Param("keyword") String keyword);

    // 내용으로 검색
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' AND n.content LIKE %:keyword% ORDER BY n.noticeCreateDate DESC")
    List<Notice> findByContentContainingAndVisible(@Param("keyword") String keyword);

    // 제목 또는 내용으로 검색
    @Query("SELECT n FROM Notice n WHERE n.isVisible = 'Y' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%) ORDER BY n.noticeCreateDate DESC")
    List<Notice> findByTitleOrContentContainingAndVisible(@Param("keyword") String keyword);
    */
}



