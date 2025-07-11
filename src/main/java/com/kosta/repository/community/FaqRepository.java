package com.kosta.repository.community;

import com.kosta.domain.community.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosta.domain.community.Faq;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    List<Faq> findByIsVisible(String isVisible);

    // 페이징 관련 메서드 추가
    Page<Faq> findByIsVisibleOrderByFaqCreateDateDesc(String isVisible, Pageable pageable);

}
