package com.kosta.domain.reivew;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_CATEGORY_ID")
  private Integer id;
  
  @Column(name = "REVIEW_CATEGORY_NAME", length = 100, unique = true)
  private String name;
}
