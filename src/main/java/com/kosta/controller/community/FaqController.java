package com.kosta.controller.community;

import java.util.List;

import com.kosta.service.community.FaqService;
import com.kosta.dto.community.FaqDTO;

import org.springframework.beans.factory.annotation.Autowired;
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
	public FaqDTO insert(@RequestBody FaqDTO dto){
		log.info("FAQ 내용 등록");
		return faqService.insertFaq(dto);
	}

	@PutMapping("/{faqNo}")
	public FaqDTO update(@PathVariable FaqDTO dto){
		log.info("FAQ 내용 수정");
		return faqService.updateFaq(dto);
	}

	@DeleteMapping("/{faqNo}")
	public void delete(@PathVariable int faqNo){
		log.info("FAQ 내용 삭제");
		faqService.deleteFaq(faqNo);
	}
}
