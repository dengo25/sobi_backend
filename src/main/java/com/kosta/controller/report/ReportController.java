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
@RequestMapping("/api/report")
@RequiredArgsConstructor 
@Slf4j                   
public class ReportController {

    private final ReportService reportService;

    // 생성자 제거 (RequiredArgsConstructor가 대신 처리)

    @PutMapping
    public ResponseEntity<String> insertReport(@RequestBody ReportDto dto) {
        reportService.saveReport(dto);
        return ResponseEntity.ok("신고가 등록되었습니다.");
    }


}