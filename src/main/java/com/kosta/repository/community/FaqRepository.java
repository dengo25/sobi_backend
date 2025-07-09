package com.kosta.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosta.domain.community.Faq;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    List<Faq> findByIsVisible(String isVisible);
}
