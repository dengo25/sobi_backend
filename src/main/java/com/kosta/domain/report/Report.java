package com.kosta.domain.report;

import java.time.LocalDateTime;

import com.kosta.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "REPORT")
public class Report {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int reportId;
	
    @ManyToOne
    @JoinColumn(name = "REPORTER_ID", referencedColumnName = "MEMBER_ID")
    private Member reporterId;

    // 신고 대상자 (foreign key: REPORTED_ID)
    @ManyToOne
    @JoinColumn(name = "REPORTED_ID", referencedColumnName = "MEMBER_ID")
    private Member reportedId;

	@Column(name = "REPORT_Type", length = 100)
	private String reportType;
	
	@Column(name = "TARGET_ID")
	private int targetId;
	
	@Column(name = "DETAIL", length = 200)
	private String detail;
	
	@Column(name = "STATUS", length = 20)
	private String status = "PENDING";
	
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @PrePersist
    public void setCreatedAt() {
      if (this.createdAt == null) {
        this.createdAt= LocalDateTime.now();
      }
    }
}
