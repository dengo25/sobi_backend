package com.kosta.mapper.community;

import com.kosta.domain.community.Faq;
import com.kosta.domain.member.Member;
import com.kosta.dto.community.FaqDTO;

public class FaqMapper {
    // Entity -> DTO
    public static FaqDTO toDTO(Faq faq){
        if(faq == null) return null;
        return FaqDTO.builder()
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
                .build();
    }

    // DTO -> Entity
    public static Faq toEntity(FaqDTO dto){
        if(dto == null) return null;

        Faq faq = new Faq();
        faq.setFaqCategory(dto.getFaqCategory());
        faq.setFaqQuestion(dto.getFaqQuestion());
        faq.setFaqAnswer(dto.getFaqAnswer());
        faq.setFaqCreateDate(dto.getFaqCreateDate());
        faq.setIsDeleted(dto.getIsDeleted());
        faq.setIsVisible(dto.getIsVisible());

        if(dto.getMemberId() != null && !dto.getMemberId().isEmpty()){
            Member member = new Member();
            member.setMemberId(dto.getMemberId());
            faq.setMember(member);
        }
        return faq;
    }
}
