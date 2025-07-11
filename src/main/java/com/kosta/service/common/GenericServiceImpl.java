package com.kosta.service.common;

import com.kosta.dto.common.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public abstract class GenericServiceImpl<T,R,ID> implements GenericService<T,R> {
    private final PagingAndSortingRepository<T,ID> repository;
    private final Function<T,R> toDtoMapper;

    protected GenericServiceImpl(PagingAndSortingRepository<T,ID> repository,
                              Function<T,R> toDtoMapper){
        this.repository = repository;
        this.toDtoMapper = toDtoMapper;
    }

    @Override
    public PageResponseDTO<R> getPage(int page, int size){
        // 각 엔티티의 실제 ID 필드명을 사용하도록 수정
        String idPropertyName = getIdPropertyName();
        Pageable pageable = PageRequest.of(page,size, Sort.by(idPropertyName).descending());
        Page<T> p = repository.findAll(pageable);

        List<R> dtos = p.getContent().stream()
                .map(toDtoMapper)
                .collect(Collectors.toList());

        return  PageResponseDTO.<R>builder()
                .content(dtos)
                .currentPage(p.getNumber())
                .pageSize(p.getSize())
                .totalElements(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .first(p.isFirst())
                .last(p.isLast())
                .hasNext(p.hasNext())
                .hasPrevious(p.hasPrevious())
                .build();
    }

    // 각 구현체에서 엔티티의 ID 필드명을 반환하도록 추상 메서드 정의
    protected abstract String getIdPropertyName();
}
