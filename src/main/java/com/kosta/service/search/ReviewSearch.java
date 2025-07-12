package com.kosta.service.search;

import com.kosta.domain.review.Review;
import com.kosta.dto.review.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ReviewSearch {
  
  Page<Review> search1(PageRequestDTO pageRequestDTO);
}
