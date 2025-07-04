package com.kosta.repository.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Category;
import com.kosta.domain.reivew.Review;
import com.kosta.repository.member.MemberRepository;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class ReviewRepositoryTest {
  
  @Autowired
  private ReviewRepository reviewRepository;
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private CategoryRepository categoryRepository;
  
  @Test
  public void test1(){
    log.info("---------------");
    log.info(reviewRepository.getClass().getName());
    
  }
  
  @Test
  public void insertCategories() {
    for (int i = 1; i <= 3; i++) {
      categoryRepository.save(Category.builder()
          .name("Category " + i)
          .build());
    }
  }
  
  @Test
  public void testInsertReviews() {
    Member member = memberRepository.findById(1L)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    
    for (Long i = 1L; i <= 113; i++) {
      Review review = Review.builder()
          .title("Test Review " + i)
          .content("Test Review Content " + i)
          .imageNumber(i)
          .confirmed("N")
          .isDeleted("N")
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .member(member) // 진짜 영속 객체
          .category(Category.builder().id(1L).build())
          .build();
      
      reviewRepository.save(review);
    }
  }
  
  @Test
  public void testPaging(){
    Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
    Page<Review> result = reviewRepository.findAll(pageable);
    
    log.info(result.getTotalElements());
    
    //getCountent가 전체 내용물
    result.getContent().stream().forEach(review -> log.info(review));
  }
}