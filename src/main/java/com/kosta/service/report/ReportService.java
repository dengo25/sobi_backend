package com.kosta.service.report;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.kosta.dto.report.ReportDetailDto;
//import com.kosta.dto.report.PagedReportListDto;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {
}