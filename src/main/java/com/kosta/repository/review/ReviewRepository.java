package com.kosta.repository.review;

import com.kosta.domain.reivew.Review;
import com.kosta.service.search.ReviewSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewSearch {
  
  Long rno(Long rno);//QueryDsl인 TodoSearch 상속
  
  
  // 여기서 선언
  @EntityGraph(attributePaths = {"member", "category"})
  // Review.member를 join fetch
  List<Review> findAll(); // 또는
  
  @EntityGraph(attributePaths = {"member", "category"})
  Page<Review> findAll(Pageable pageable); // 페이징까지 적용할 경우
  
  //Optional<Review> findById(Long rno);
  Optional<Review> findByRno(Long rno);
  
}
