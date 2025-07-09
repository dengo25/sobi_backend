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
public class ReportListDto {

	private String reporterId;
	private String reportedId;
	private String reportType;
	private String status;
	private LocalDateTime createdAt;
	
}
