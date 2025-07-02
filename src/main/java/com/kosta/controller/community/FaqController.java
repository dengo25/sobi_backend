package com.kosta.controller.community;

import java.util.List;

import com.kosta.service.community.FaqService;
import com.kosta.dto.community.FaqDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
