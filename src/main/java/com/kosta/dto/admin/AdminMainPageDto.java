package com.kosta.dto.admin;

import java.util.List;


import com.kosta.dto.blacklist.BlacklistDto;
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
	private List<BlacklistDto> blacklistDto;
	private long unSolvedReportCount;
}
