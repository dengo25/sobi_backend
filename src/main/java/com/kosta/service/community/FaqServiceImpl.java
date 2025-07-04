package com.kosta.service.community;

import com.kosta.domain.community.Faq;
import com.kosta.dto.community.FaqDTO;
import com.kosta.mapper.community.FaqMapper;
import com.kosta.repository.community.FaqRepository;
import com.kosta.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.kosta.mapper.community.FaqMapper.toDTO;
import static com.kosta.mapper.community.FaqMapper.toEntity;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqServiceImpl implements FaqService{
    private final FaqRepository faqRepository;

    public List<FaqDTO> getAllFaqs (){
        return faqRepository.findByIsVisible("Y").stream()
                .map(FaqMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FaqDTO insertFaq(FaqDTO dto){
        Faq faq = toEntity(dto);
        faq.setFaqAnswer(HtmlSanitizer.strictSanitize(faq.getFaqAnswer()));
        faq.setFaqCreateDate(new Date());
        faq.setIsDeleted("N");
        faq.setIsVisible("Y");

        return toDTO(faqRepository.save(faq));
    }

    public FaqDTO updateFaq(FaqDTO dto){
        Faq faq = faqRepository.findById(dto.getFaqNo())
                .orElseThrow(() -> new RuntimeException("해당하는 Faq 를 찾지 못했습니다."));

        faq.setFaqCategory(dto.getFaqCategory());
        faq.setFaqQuestion(dto.getFaqQuestion());
        faq.setFaqAnswer(HtmlSanitizer.strictSanitize(dto.getFaqAnswer()));
        faq.setFaqEditDate(new Date());

        return toDTO(faqRepository.save(faq));
    }

    public void deleteFaq(int faqNo){
        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(() -> new RuntimeException("해당하는 Faq 를 찾지 못했습니다."));

        faq.setIsVisible("N");
        faq.setFaqDelete(new Date());
        // faqRepository.deleteById(faqNo); // 실제 삭제
    }
}
