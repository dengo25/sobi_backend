package com.kosta.service.report;

import java.util.List;
import com.kosta.dto.report.ReportDto;
import com.kosta.dto.report.ReportListDto;
import com.kosta.dto.report.ReportSearchDto;
import com.kosta.dto.report.ReportPageResponse;

public interface ReportService {

    public void saveReport(ReportDto dto);
    
    // 기존 메서드 유지
    public List<ReportListDto> getReportList(String status, String type);
    
    // 페이징 기능 추가
    public ReportPageResponse getReportsWithFiltersAndPaging(ReportSearchDto searchDto);
}