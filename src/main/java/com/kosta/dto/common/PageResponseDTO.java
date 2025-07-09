package com.kosta.dto.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO<T> {
    private List<T> content;    // 사용될 실제 데이터
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
}
