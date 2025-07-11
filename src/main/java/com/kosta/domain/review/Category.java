package com.kosta.domain.review;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Category {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_CATEGORY_ID")
  private Long id;
  
  @Column(name = "REVIEW_CATEGORY_NAME", length = 100, unique = true)
  private String name;
}
