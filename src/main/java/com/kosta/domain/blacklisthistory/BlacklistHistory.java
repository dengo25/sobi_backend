package com.kosta.domain.blacklisthistory;

import java.util.Date;

import com.kosta.domain.blacklist.Blacklist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "BLACKLIST_HISTORY")
public class BlacklistHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int blacklistHistoryNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BLACKLIST_NO")
	private Blacklist blacklist;
	
	@Column(name = "DETAIL", length = 200)
	private String detail;
	
	@Column(name = "REPORT_TYPE", length = 100)
	private String reportType;
	
//	@Column(name = "HISTORY_CREATED_AT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//	private Date historyCreatedAt;
	
	
}
