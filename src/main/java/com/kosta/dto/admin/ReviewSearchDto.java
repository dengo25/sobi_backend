package com.kosta.dto.admin;

import lombok.Data;

@Data
public class ReviewSearchDto {
    private int page = 1;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
    private String confirmed;
}
