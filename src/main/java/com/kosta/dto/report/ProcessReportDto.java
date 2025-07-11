package com.kosta.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessReportDto {
    private String action;    // "APPROVE" or "REJECT"
    private String reason;    // 처리 사유
}