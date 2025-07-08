package com.kosta.dto.admin;

import java.util.List;

import com.kosta.domain.memberlog.Memberlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMainPageDto {
	private long totalMemberCount;
	private long blockedCount;
	private long reviewCount;
	private List<Memberlog> memberlogs;
	private long unSolvedReportCount;
}
