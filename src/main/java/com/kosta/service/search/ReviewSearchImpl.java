package com.kosta.service.search;

import com.kosta.domain.review.QReview;
import com.kosta.domain.review.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch{
  public ReviewSearchImpl() {
    super(Review.class);
  }
  
  @Override
  public Page<Review> search1(PageRequestDTO pageRequestDTO) {
    
    log.info("search1 - pageRequestDTO: {}", pageRequestDTO);
    
    QReview review = QReview.review;
    
    JPQLQuery<Review> query = from(review);
    
    query.leftJoin(review.images).fetchJoin();
    query.leftJoin(review.member).fetchJoin();
    query.leftJoin(review.category).fetchJoin(); 
    query.select(review).distinct();
    
    query.where(review.isDeleted.ne("Y").or(review.isDeleted.isNull()));
    
    if (pageRequestDTO.getCategory() != null && pageRequestDTO.getCategory() > 0) {
      log.info("카테고리 필터링 적용 - categoryId: {}", pageRequestDTO.getCategory());
      query.where(review.category.id.eq(pageRequestDTO.getCategory()));
    }
    
    if (pageRequestDTO.getKeyword() != null && !pageRequestDTO.getKeyword().trim().isEmpty()) {
      log.info("키워드 검색 적용 - keyword: {}", pageRequestDTO.getKeyword());
      String keyword = "%" + pageRequestDTO.getKeyword().trim() + "%";
      query.where(
        review.title.likeIgnoreCase(keyword)
        .or(review.content.likeIgnoreCase(keyword))
      );
    }
    
    Sort sort = Sort.by("rno").descending(); // 기본: 최신순
    if ("oldest".equals(pageRequestDTO.getSort())) {
      sort = Sort.by("rno").ascending(); // 오래된순
    }
    
    Pageable pageable = PageRequest.of(
        pageRequestDTO.getPage() - 1,
        pageRequestDTO.getSize(),
        sort
    );
    
    this.getQuerydsl().applyPagination(pageable, query);
    
    List<Review> list = query.fetch();
    long total = query.fetchCount();
    
    log.info("검색 결과 - total: {}, listSize: {}", total, list.size());
    
    return new PageImpl<>(list, pageable, total);
  }
}