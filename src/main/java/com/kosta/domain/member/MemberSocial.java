package com.kosta.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER_SOCIAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSocial {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "MEMBER_SOCIAL_ID")
  private String  memberSocialId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID", unique = true)
  private Member member;
  
  @Column(name = "SOCIAL_TYPE",length = 20)
  private String socialType;
  
  @Column(name = "LINKED_AT")
  private LocalDateTime linkedAt;

  
}
