package com.kosta.repository.memberlog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.domain.memberlog.Memberlog;

public interface MemberLogRepository extends JpaRepository<Memberlog, Integer> {
	
}
