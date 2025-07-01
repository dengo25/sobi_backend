package com.kosta.domain.memberlog;

import java.time.LocalDateTime;

import com.kosta.domain.member.Member;

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
import lombok.Data;

@Entity
@Data
@Table(name = "MEMBER_LOG")
public class Memberlog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int logNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	@Column(name="ACCESSED_MENU", length=20)
	private String accessedMenu;
	
	@Column(name = "MEMBER_ACTION_TYPE", length = 20)
	private String memberActionType;
	
	@Column(name = "MEBER_LOG_CREATED")
	private LocalDateTime memberLogCreated;
	
	@PrePersist
	public void setLogCreated() {
		if(this.memberLogCreated == null) {
			this.memberLogCreated = LocalDateTime.now();
		}
	}
}
