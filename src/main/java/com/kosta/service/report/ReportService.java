package com.kosta.service.report;

import com.kosta.dto.report.*;

public interface ReportService {

    public void saveReport(ReportDto dto);
    
    public ReportPageResponse getReportsWithFiltersAndPaging(ReportSearchDto searchDto);
    
}