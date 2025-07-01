package com.kosta.domain.report;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "REPORT")
public class Report {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int reportId;
	
	@Column(name = "REPORTER_ID", length = 20)
	private String reporterId;

	@Column(name = "REPORTED_ID", length = 20)
	private String reportedId;

	@Column(name = "REPORT_CATEGORY", length = 20)
	private String reportCategory;
	
	@Column(name = "TARGET_ID")
	private int targetId;
	
	@Column(name = "DETAIL", length = 200)
	private String detail;
	
	@Column(name = "STATUS", length = 20)
	private String status = "PEDNING";
}
