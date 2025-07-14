package com.kosta.service.report;

import java.util.List;
import com.kosta.dto.report.*;

public interface ReportService {

    public void saveReport(ReportDto dto);
    
    public List<ReportListDto> getReportList(String status, String type);
    
    public ReportPageResponse getReportsWithFiltersAndPaging(ReportSearchDto searchDto);
    
    public ReportDetailDto getReportDetail(int reportId);
    
    public void processReport(int reportId, ProcessReportDto processDto);
}