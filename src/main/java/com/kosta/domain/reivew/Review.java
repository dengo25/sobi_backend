package com.kosta.domain.reivew;

import com.kosta.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_ID")
  private Long rno;
  
  @Column(name = "REVIEW_TITLE",length = 500)
  private String title;
  
  
  @Column(name = "CONTENT", columnDefinition = "TEXT")
  private String content;
  
  @Column(name = "REVIEW_IMAGE_NUMBER")
  private Long imageNumber;
  
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;
  
  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;
  
  @Column(name = "IS_DELETED", length = 1)
  private String isDeleted;
  
  @Column(name = "CONFIRMED", length = 1)
  private String confirmed;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
  private Member member;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REVIEW_CATEGORY_ID")
  private Category category;
  
  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ReviewImage> images = new ArrayList<>();
  
  // 저장 직전에 자동 실행
  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
    
    if (this.confirmed == null) {
      this.confirmed = "N";
    }
  }
  
  // 수정 직전에 자동 실행
  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now(); // 수정 시에만 갱신
  }
  
  public void addImage(String fileUrl, String originalFileName, String fileType, String isThumbnail) {
    ReviewImage image = ReviewImage.builder()
        .fileUrl(fileUrl)
        .originalFileName(originalFileName)
        .fileType(fileType)
        .isThumbnail(isThumbnail)
        .review(this) //외래키 연관관계 주입
        .build();
    
    this.images.add(image); //리스트추가
  }
  
}