package com.kosta.dto.blacklist;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlacklistDto {
    private int blackListNo;
    private String memberId;
    private String memberName;
    private LocalDateTime updateAt;
}