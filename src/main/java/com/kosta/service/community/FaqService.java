package com.kosta.service.community;

import java.util.List;

import com.kosta.dto.community.FaqDTO;

public interface FaqService {
    List<FaqDTO> getAllFaqs();
    FaqDTO insertFaq(FaqDTO dto);
    FaqDTO updateFaq(FaqDTO dto);
    void deleteFaq(int faqNo);
}
