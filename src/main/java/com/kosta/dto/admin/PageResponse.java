package com.kosta.dto.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;      // 실제 데이터 리스트
    private int page;             // 현재 페이지 번호
    private int size;             // 페이지 당 데이터 개수
    private long totalElements;  // 전체 데이터 수
    private int totalPages;       // 전체 페이지 수
}
