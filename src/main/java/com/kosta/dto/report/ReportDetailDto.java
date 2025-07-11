package com.kosta.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailDto {
    private int reportId;
    private String reporterId;
    private String reporterName;
    private String reportedId;
    private String reportedName;
    private String reportType;
    private String detail;
    private int targetId;
    private String status;
    private LocalDateTime createdAt;
}