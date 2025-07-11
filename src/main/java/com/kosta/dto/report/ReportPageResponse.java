// 2. 페이징된 응답을 위한 DTO
package com.kosta.dto.report;

import lombok.Data;
import java.util.List;

@Data
public class ReportPageResponse {
    private List<ReportListDto> reports; // 신고 목록
    private long totalElements;          // 전체 항목 수
    private int totalPages;              // 전체 페이지 수
    private int currentPage;             // 현재 페이지
    private int pageSize;                // 페이지 크기
    private boolean hasNext;             // 다음 페이지 존재 여부
    private boolean hasPrevious;         // 이전 페이지 존재 여부
    
    public static ReportPageResponse of(List<ReportListDto> reports, long totalElements, 
                                       int currentPage, int pageSize) {
        ReportPageResponse response = new ReportPageResponse();
        response.setReports(reports);
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
        response.setCurrentPage(currentPage);
        response.setPageSize(pageSize);
        response.setHasNext(currentPage < response.getTotalPages() - 1);
        response.setHasPrevious(currentPage > 0);
        return response;
    }
}