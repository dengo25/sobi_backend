package com.kosta.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {
  @Id
  private String id;
  
}
