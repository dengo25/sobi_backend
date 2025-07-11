package com.kosta.repository.report;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.kosta.domain.report.Report;
import java.util.Optional;
@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    // 특정 회원이 신고 받은 횟수 조회
    long countByReportedIdMemberId(String memberId);
    
 // 기존 메서드 (상태와 타입으로만 필터링)
    @Query("SELECT r FROM Report r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:reportType IS NULL OR r.reportType = :reportType) " +
           "ORDER BY r.createdAt DESC")
    List<Report> findByStatusAndType(@Param("status") String status, 
                                   @Param("reportType") String reportType);
    
    // 페이징과 복합 필터링을 위한 메서드 (Member 관계 고려)
    @Query("SELECT r FROM Report r WHERE " +
           "(:reporterId IS NULL OR r.reporterId.memberId = :reporterId) AND " +
           "(:reportedId IS NULL OR r.reportedId.memberId = :reportedId) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:reportType IS NULL OR r.reportType = :reportType)")
    Page<Report> findReportsWithFilters(
        @Param("reporterId") String reporterId,
        @Param("reportedId") String reportedId,
        @Param("status") String status,
        @Param("reportType") String reportType,
        Pageable pageable
    );
    
    // 통계를 위한 메서드들
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.reportType = :reportType")
    long countByReportType(@Param("reportType") String reportType);
    
    

	 //신고 상세 조회 (회원 정보 포함)
	 @Query("SELECT r FROM Report r " +
	        "JOIN FETCH r.reporterId " +
	        "JOIN FETCH r.reportedId " +
	        "WHERE r.reportId = :reportId")
	 Optional<Report> findByIdWithMembers(@Param("reportId") int reportId);
}