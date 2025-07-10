package com.kosta.service.common;

import com.kosta.dto.common.PageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface GenericService<T,R> {
    PageResponseDTO<R> getPage(int page, int size);
    PageResponseDTO<R> getListWithPaging(Pageable pageable);
}
