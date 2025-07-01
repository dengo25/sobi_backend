package com.kosta.service.search;

import com.kosta.domain.reivew.QReview;
import com.kosta.domain.reivew.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch{
  public ReviewSearchImpl() {
    
    super(Review.class); //QuerydslRepositorySupport에 Review 엔티티를 넘겨서 기본 설정
  }
  
  @Override
  public Page<Review> search1(PageRequestDTO pageRequestDTO) {
    
    log.info("search1..............."); //동작확인
    
    QReview review = QReview.review; //쿼리를 날리기위한 객체(자동생성된 Q클래스)
    
    //아래 상태에서 조건도, 페이징도 없다
    JPQLQuery<Review> query = from(review); //상속을 받았기 때문에 from을 이용해서 뽑아낸다
    
    //Spring Data의 PageRequest를 이용해서 페이징 정보생성
    Pageable pageable = PageRequest.of(
        pageRequestDTO.getPage() - 1,
        pageRequestDTO.getSize(),
        Sort.by("rno").descending());
    
    //위에서 만든 query에 적용
    this.getQuerydsl().applyPagination(pageable, query); //pageable을 그대로 가져오고 쿼리문 그대로 처리
    
    List<Review> list = query.fetch(); //fetch를 이용해서 쿼리를 날린다. 목록 데이터를 가져올 때 쓴다.
    
    //총 레코드 개수
    long total = query.fetchCount(); //반환타입은 long으로 나온다.
    
    
    return new PageImpl<>(list, pageable, total);
  }
}
