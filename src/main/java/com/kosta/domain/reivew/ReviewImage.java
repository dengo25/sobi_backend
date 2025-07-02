package com.kosta.domain.reivew;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_IMAGE_ID")
  private Long ino;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REVIEW_ID", nullable = false)
  private Review review;
  
  @Column(name = "FILE_URL", nullable = false, length = 500)
  private String fileUrl;
  
  @Column(name = "ORIGINAL_FILE_NAME", nullable = false, length = 255)
  private String originalFileName;
  
  @Column(name = "FILE_TYPE", nullable = false, length = 50)
  private String fileType;
  
  @Column(name = "IS_THUMBNAIL", length = 1)
  private String isThumbnail; // 나중에 첫번쨰 이미지만 Y로 저장
}
