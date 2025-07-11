package com.kosta.dto.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PageResponseDTO<T> {
    private List<T> content;    // 사용될 실제 데이터
    private int currentPage;    // 현재 페이지
    private int totalPages;     // 전체 페이지
    private long totalElements; // 전체 데이터 건수
    private int pageSize;       // 페이지 크기
    private boolean first;      // 첫번쨰 페이지
    private boolean last;       // 마지막 페이지
    private boolean hasNext;    // 다음
    private boolean hasPrevious;// 이전
}
