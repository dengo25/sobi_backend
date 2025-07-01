package com.kosta.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.domain.community.Faq;

public interface FaqRepository extends JpaRepository<Faq, Integer> {

}
