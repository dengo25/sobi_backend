package com.kosta.service.review;

import com.kosta.dto.review.CategoryDTO;
import com.kosta.repository.review.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
  
  private final CategoryRepository categoryRepository;
  
  public List<CategoryDTO> getAllCategories() {
    return categoryRepository.findAll()
        .stream()
        .map(category -> CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .build())
        .collect(Collectors.toList());
  }
}