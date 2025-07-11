package com.kosta.domain.blacklist;

import java.time.LocalDateTime;

import com.kosta.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BLACK_LIST")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int blackListNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Member member;
	
	@Column(name = "UPDATE_AT")
	private LocalDateTime updateAt;
	
	@Column(name = "STATUS", length = 20)
	private String status;
	
	@PrePersist
	public void setUpdateAt() {
		if(this.updateAt == null) {
			this.updateAt = LocalDateTime.now();
		}
	}
	
}
