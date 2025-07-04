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
public class ReportDto {
	
	private String detail;
	private String reportType;
	private String reportedId;
	private String reporterId;
	private int targetId;
	private LocalDateTime createdAt;
}
