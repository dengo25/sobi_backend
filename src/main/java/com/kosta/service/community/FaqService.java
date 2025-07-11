package com.kosta.service.community;

import java.util.List;

import com.kosta.domain.community.Faq;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.FaqDTO;
import com.kosta.service.common.GenericService;
import org.springframework.data.domain.Pageable;

public interface FaqService extends GenericService<Faq, FaqDTO> {
    List<FaqDTO> getAllFaqs();
    PageResponseDTO<FaqDTO> getListWithPaging(Pageable pageable);
    FaqDTO insertFaq(FaqDTO dto);
    FaqDTO updateFaq(FaqDTO dto);
    void deleteFaq(int faqNo);
}
