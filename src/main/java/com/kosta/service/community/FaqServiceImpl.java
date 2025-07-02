package com.kosta.service.community;

import com.kosta.dto.community.FaqDTO;
import com.kosta.repository.community.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqServiceImpl implements FaqService{
    private final FaqRepository faqRepository;

    public List<FaqDTO> getAllFaqs (){
        return faqRepository.findAll().stream()
                .map(faq -> FaqDTO.builder()
                        .faqNo(faq.getFaqNo())
                        .memberId(faq.getMember() != null ? faq.getMember().getMemberId() : null)
                        .faqCategory(faq.getFaqCategory())
                        .faqQuestion(faq.getFaqQuestion())
                        .faqAnswer(faq.getFaqAnswer())
                        .faqCreateDate(faq.getFaqCreateDate())
                        .faqEditDate(faq.getFaqEditDate())
                        .faqDelete(faq.getFaqDelete())
                        .isDeleted(faq.getIsDeleted())
                        .isVisible(faq.getIsVisible())
                        .build())
                .collect(Collectors.toList());
    }
}
