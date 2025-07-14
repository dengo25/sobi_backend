package com.kosta.controller.report;

import com.kosta.dto.report.ProcessReportDto;
import com.kosta.dto.report.ReportDetailDto;
import com.kosta.dto.report.ReportDto;
import com.kosta.dto.report.ReportPageResponse;
import com.kosta.dto.report.ReportSearchDto;
import com.kosta.service.report.ReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin/report")
@RequiredArgsConstructor  // 🆕 추가
@Slf4j                   // 🆕 추가 (log 사용을 위해)
public class ReportController {

    private final ReportService reportService;

    // 🆕 생성자 제거 (RequiredArgsConstructor가 대신 처리)

    @PutMapping
    public ResponseEntity<String> insertReport(@RequestBody ReportDto dto) {
        reportService.saveReport(dto);
        return ResponseEntity.ok("신고가 등록되었습니다.");
    }

    /**
     * 새로운 방식: 필터링 + 페이징
     * GET /api/reports?page=0&size=10&sortBy=createdAt&sortDir=desc&status=PENDING
     */
    @GetMapping
    public ResponseEntity<ReportPageResponse> getReports(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "reporterId", required = false) String reporterId,
            @RequestParam(name = "reportedId", required = false) String reportedId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "reportType", required = false) String reportType) {

        // 검색 조건 객체 생성
        ReportSearchDto searchDto = new ReportSearchDto();
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy(sortBy);
        searchDto.setSortDir(sortDir);
        searchDto.setReporterId(reporterId);
        searchDto.setReportedId(reportedId);
        searchDto.setStatus(status);
        searchDto.setReportType(reportType);

        ReportPageResponse response = reportService.getReportsWithFiltersAndPaging(searchDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 신고 상세 조회
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailDto> getReportDetail(@PathVariable int reportId) {
        try {
            ReportDetailDto reportDetail = reportService.getReportDetail(reportId);
            return ResponseEntity.ok(reportDetail);
        } catch (RuntimeException e) {
            log.error("신고 상세 조회 오류 - ID: {}, 오류: {}", reportId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 신고 처리 (승인/반려)
     */
    @PutMapping("/{reportId}/process")
    public ResponseEntity<String> processReport(
            @PathVariable int reportId,
            @RequestBody ProcessReportDto processDto) {

        try {
            reportService.processReport(reportId, processDto);

            String action = "APPROVE".equals(processDto.getAction()) ? "승인" : "반려";
            return ResponseEntity.ok("신고가 " + action + "되었습니다.");

        } catch (RuntimeException e) {
            log.error("신고 처리 오류 - ID: {}, 오류: {}", reportId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}