package com.kosta.repository.blacklist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kosta.domain.blacklist.Blacklist;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
	
	@Query("SELECT b FROM Blacklist b WHERE b.status = 'BLOCKED'")
	public List<Blacklist> getBlockedMember();
	
	long countByStatus(String status);
}
