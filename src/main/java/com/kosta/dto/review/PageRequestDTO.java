package com.kosta.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    
    @Builder.Default
    private int page = 1;
    
    @Builder.Default
    private int size = 10;
    
    private Long category;
    
    private String keyword;

    private String sort;
}