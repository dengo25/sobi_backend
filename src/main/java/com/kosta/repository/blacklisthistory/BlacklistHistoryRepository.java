package com.kosta.repository.blacklisthistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kosta.domain.blacklisthistory.BlacklistHistory;

import jakarta.transaction.Transactional;

@Repository
public interface BlacklistHistoryRepository extends JpaRepository<BlacklistHistory, Integer> {
	@Query("SELECT bh FROM BlacklistHistory bh WHERE bh.blacklist.member.memberId= : memberId")
	public List<BlacklistHistory> blacklistHistories(String memberId);
}
