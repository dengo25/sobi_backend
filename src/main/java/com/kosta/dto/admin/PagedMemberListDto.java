// 페이징 응답을 위한 래퍼 클래스
package com.kosta.dto.admin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedMemberListDto {
    private List<MemberListDto> members;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}

// 또는 Spring Data의 Page를 그대로 사용하는 방법도 있습니다