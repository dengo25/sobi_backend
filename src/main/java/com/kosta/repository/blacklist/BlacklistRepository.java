package com.kosta.repository.blacklist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.member.Member;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    
    // 기존 메서드들
    long countByStatus(String status);
    
    @Query("SELECT b FROM Blacklist b JOIN FETCH b.member WHERE b.status = 'BLOCKED'")
    List<Blacklist> getBlockedMember();
    
    // 특정 회원의 블랙리스트 상태 확인
    Optional<Blacklist> findById(int blacklistNo);
    
    // 특정 회원의 활성 블랙리스트 확인
    @Query("SELECT b FROM Blacklist b WHERE b.member = :member AND b.status = 'BLOCKED'")
    Optional<Blacklist> findActiveBlacklistByMember(@Param("member") Member member);
    
    // 블랙리스트 존재 여부 확인
    boolean existsByMemberAndStatus(Member member, String status);
}