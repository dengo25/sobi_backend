package com.kosta.domain.blacklist;

import java.util.Date;

import com.kosta.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "BLACK_LIST")
@Data
public class Blacklist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int blackListNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
//	@Column(name = "UPDATE_AT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
//	private Date updateAt;
	
	@Column(name = "STATUS", length = 20)
	private String status = "BLOCKED";
	
}
