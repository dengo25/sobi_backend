package com.kosta.controller.community;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.domain.community.Faq;
import com.kosta.dto.community.FaqDTO;
import com.kosta.repository.community.FaqRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController()
@RequestMapping("/api/faq")
public class FaqController {
	@Autowired
	FaqRepository faqRepository;
	
	
	private String name = "MacUser";
	
	
	@GetMapping("")
	public List<Faq> list() {
//		FaqDTO dto = //FaqDTO.builder()
//				dto.faqNo(1)
//	            .faqQuestion("Q?")
//	            .faqAnswer("A!")
//	            .build();
//
//	        System.out.println(dto);
		
//	    List<FaqDTO> list = faqRepository.findAll().stream()
//	            .map(faq -> FaqDTO.builder()
//	                    .faqNo(faq.getFaqNo())
//	                    .memberId(faq.getMember() != null ? faq.getMember().getMemberId() : null)
//	                    .faqCategory(faq.getFaqCategory())
//	                    .faqQuestion(faq.getFaqQuestion())
//	                    .faqAnswer(faq.getFaqAnswer())
//	                    .faqCreateDate(faq.getFaqCreateDate())
//	                    .faqEditDate(faq.getFaqEditDate())
//	                    .faqDelete(faq.getFaqDelete())
//	                    .isDeleted(faq.getIsDeleted())
//	                    .isVisible(faq.getIsVisible())
//	                    .build())
//	                .collect(Collectors.toList()); // ✅ Java 8 호환 방식

	    // 콘솔에 출력
	    //System.out.println("===== FAQ JSON =====");
	    //list.forEach(System.out::println); // toString()이 잘 정의돼 있어야 출력이 잘됨
	    //log.info("Logtest : {}");
		log.info("Hello Lombok");
	    
	    //List<Faq> faqs = faqRepository.findAll();
        return faqRepository.findAll();
        // 콘솔에 Entity 출력해서 데이터 확인
//        System.out.println("===== FAQ Entity Data =====");
//        faqs.forEach(faq -> {
//            System.out.println("FAQ No: " + faq.getFaqNo());
//            System.out.println("Category: " + faq.getFaqCategory());
//            System.out.println("Question: " + faq.getFaqQuestion());
//            System.out.println("Answer: " + faq.getFaqAnswer());
//            System.out.println("Create Date: " + faq.getFaqCreateDate());
//            System.out.println("Is Deleted: " + faq.getIsDeleted());
//            System.out.println("Is Visible: " + faq.getIsVisible());
//            System.out.println("---");
//        });
//	    
//	    return faqs;
	}

}
