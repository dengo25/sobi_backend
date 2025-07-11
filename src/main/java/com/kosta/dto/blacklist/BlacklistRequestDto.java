package com.kosta.dto.blacklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistRequestDto {
    private String memberId;           // 블랙리스트 대상 회원 ID
    private String reason;             // 블랙리스트 추가 사유
    private String reportType;         // 신고 유형
    private Integer reportId;          // 연관된 신고 ID
}