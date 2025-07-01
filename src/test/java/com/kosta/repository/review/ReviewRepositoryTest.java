package com.kosta.repository.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Category;
import com.kosta.domain.reivew.Review;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class ReviewRepositoryTest {
  
  @Autowired
  private ReviewRepository reviewRepository;
  
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
    for (int i = 1; i <= 10; i++) {
      Review review = Review.builder()
          .title("Test Review " + i)
          .content("This is test content for review " + i)
          .imageNumber(i)
          .confirmed("N")
          .isDeleted("N")
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .member(Member.builder().id(1L).build()) //
          .category(Category.builder().id(1).build()) //
          .build();
      
      reviewRepository.save(review);
    }
  }
  
}