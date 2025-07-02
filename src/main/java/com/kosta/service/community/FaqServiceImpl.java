package com.kosta.service.community;

import com.kosta.domain.community.Faq;
import com.kosta.domain.member.Member;
import com.kosta.dto.community.FaqDTO;
import com.kosta.repository.community.FaqRepository;
import com.kosta.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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


    public FaqDTO insertFaq(FaqDTO dto){
        Faq faq = new Faq();
        faq.setFaqCategory(dto.getFaqCategory());
        faq.setFaqQuestion(dto.getFaqQuestion());

        String safeAnswer = HtmlSanitizer.strictSanitize(dto.getFaqAnswer());
        faq.setFaqAnswer(safeAnswer);

        faq.setFaqCreateDate(new Date());
        faq.setIsDeleted("N");
        faq.setIsVisible("Y");

        if(dto.getMemberId() != null && !dto.getMemberId().isEmpty()){
            Member member = new Member();
            member.setMemberId(dto.getMemberId());
            faq.setMember(member);
        }

        Faq saved = faqRepository.save(faq);

        return FaqDTO.builder()
                        .faqNo(saved.getFaqNo())
                        .memberId(saved.getMember() != null ? saved.getMember().getMemberId() : null)
                        .faqCategory(saved.getFaqCategory())
                        .faqQuestion(saved.getFaqQuestion())
                        .faqAnswer(saved.getFaqAnswer())
                        .faqCreateDate(saved.getFaqCreateDate())
                        .faqEditDate(saved.getFaqEditDate())
                        .faqDelete(saved.getFaqDelete())
                        .isDeleted(saved.getIsDeleted())
                        .isVisible(saved.getIsVisible())
                        .build();
    }

    public void deleteFaq(int faqNo){
        faqRepository.deleteById(faqNo);
    }
}
