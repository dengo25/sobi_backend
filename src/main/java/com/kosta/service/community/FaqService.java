package com.kosta.service.community;

import java.util.List;

import com.kosta.domain.community.Faq;
import com.kosta.dto.community.FaqDTO;
import com.kosta.service.common.GenericService;

public interface FaqService extends GenericService<Faq, FaqDTO> {
    List<FaqDTO> getAllFaqs();
    FaqDTO insertFaq(FaqDTO dto);
    FaqDTO updateFaq(FaqDTO dto);
    void deleteFaq(int faqNo);
}
