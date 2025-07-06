package com.kosta.controller.community;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import com.kosta.domain.member.Member;
import com.kosta.service.community.FaqService;
import com.kosta.dto.community.FaqDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController()
@RequestMapping("/api/faq")
public class FaqController {
	@Autowired
	private FaqService faqService;
	
	@GetMapping("")
	public List<FaqDTO> list() {
		log.info("FAQ 전체 목록 조회 요청");
		return faqService.getAllFaqs();
	}

	@PostMapping("")
	public FaqDTO insert(@RequestBody FaqDTO dto, @AuthenticationPrincipal String memberId, Principal principal ){
		// 현재 권한 목록 확인
		Collection<? extends GrantedAuthority> authorities =
				SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		authorities.forEach(auth -> log.info("현재 권한: {}", auth.getAuthority()));

		log.info("접근자 ID: {}", principal.getName());
		log.info("FAQ 등록 요청자 = {}", memberId);
		log.info("FAQ 내용 등록");
		dto.setMemberId(memberId);
		return faqService.insertFaq(dto);
	}

	@PutMapping("/{faqNo}")
	public FaqDTO update(@RequestBody FaqDTO dto, @PathVariable("faqNo") int faqNo, Principal principal, @AuthenticationPrincipal String memberId){
		log.info("FAQ 내용 수정 요청: faqNo = {}, 사용자 = {}, 사용자 아이디={}", faqNo, principal.getName(), memberId);
		dto.setFaqNo(faqNo); // 또는 필요시 dto에 FAQ 번호 설정
		return faqService.updateFaq(dto);
	}

	@DeleteMapping("/{faqNo}")
	public void delete(@PathVariable int faqNo){
		log.info("FAQ 내용 삭제");
		faqService.deleteFaq(faqNo);
	}
}
