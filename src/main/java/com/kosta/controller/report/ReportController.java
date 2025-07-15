package com.kosta.controller.report;

import com.kosta.dto.report.ReportDto;
import com.kosta.service.report.ReportService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor 
@Slf4j                   
public class ReportController {

    private final ReportService reportService;

    @PutMapping
    public ResponseEntity<String> insertReport(@RequestBody ReportDto dto) {
        reportService.saveReport(dto);
        return ResponseEntity.ok("신고가 등록되었습니다.");
    }


}