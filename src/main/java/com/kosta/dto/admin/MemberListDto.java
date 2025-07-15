package com.kosta.dto.admin;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberListDto {
	private String memberName;
	  private String memberId;
	  private LocalDateTime memberReg;
	  private String memberGender;
	  private String memberEmail;
	  private String memberAddr;
	  private long memberReviewCount;
	  private long memberReportCount;
}
