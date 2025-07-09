// 1. 검색 조건을 위한 DTO
package com.kosta.dto.report;

import lombok.Data;

@Data
public class ReportSearchDto {
    private String reporterId;        // 신고자 ID로 필터링
    private String reportedId;        // 피신고자 ID로 필터링
    private String status;            // 신고 상태로 필터링 (PENDING, PROCESSED, REJECTED 등)
    private String reportType;        // 신고 유형으로 필터링 (SPAM, ABUSE, INAPPROPRIATE 등)
    
    // 페이징 정보
    private int page = 0;             // 현재 페이지 (0부터 시작)
    private int size = 10;            // 한 페이지당 항목 수
    private String sortBy = "createdAt"; // 정렬 기준 필드 (기본값: 생성일시)
    private String sortDir = "desc";    // 정렬 방향 (asc, desc)
}