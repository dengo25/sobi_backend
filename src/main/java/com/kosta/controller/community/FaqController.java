package com.kosta.controller.community;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.domain.community.Faq;
import com.kosta.repository.community.FaqRepository;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController()
@RequestMapping("/api/faq")
public class FaqController {
	@Autowired
	FaqRepository faqRepository;
	
	@GetMapping("/")
	public List<Faq> list() {
		return faqRepository.findAll();
		//model.addAttribute("list", faqRepository.findAll());
		
		//return "list";
	}
}
