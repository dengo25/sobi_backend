package com.kosta.domain.blacklisthistory;

import java.time.LocalDateTime;

import com.kosta.domain.blacklist.Blacklist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "BLACKLIST_HISTORY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Column(name = "HISTORY_CREATED_AT")
	private LocalDateTime historyCreatedAt;
	
	@PrePersist
	public void setCreatedAt() {
		if(this.historyCreatedAt == null) {
			this.historyCreatedAt = LocalDateTime.now();
		}
	}
}
