package com.kosta.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

	private String reporterId;
	private String reportedId;
	private String detail;
	private String reportType;
	private int targetId;
}
