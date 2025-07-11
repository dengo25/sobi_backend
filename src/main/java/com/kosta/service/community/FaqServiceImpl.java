package com.kosta.service.community;

import com.kosta.domain.community.Faq;
import com.kosta.domain.community.Notice;
import com.kosta.domain.member.Member;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.FaqDTO;
import com.kosta.dto.community.NoticeDTO;
import com.kosta.mapper.community.FaqMapper;
import com.kosta.mapper.community.NoticeMapper;
import com.kosta.repository.community.FaqRepository;
import com.kosta.repository.community.NoticeRepository;
import com.kosta.repository.member.MemberRepository;
import com.kosta.service.common.GenericServiceImpl;
import com.kosta.util.HtmlSanitizer;
import com.kosta.util.S3PresignedService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kosta.mapper.community.FaqMapper.toDTO;
import static com.kosta.mapper.community.FaqMapper.toEntity;

@Service
// @RequiredArgsConstructor
@Transactional
public class FaqServiceImpl extends GenericServiceImpl<Faq, FaqDTO, Integer>
        implements FaqService{
    private static final Logger log = LoggerFactory.getLogger(FaqServiceImpl.class);
    private final FaqRepository faqRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FaqServiceImpl(FaqRepository faqRepository,
                             MemberRepository memberRepository) {
        super(faqRepository, FaqMapper::toDTO);
        this.faqRepository = faqRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    protected String getIdPropertyName() {
        return "faqNo";  // Faq 엔티티 PK 필드명
    }


    public List<FaqDTO> getAllFaqs (){
        return faqRepository.findByIsVisible("Y").stream()
                .map(FaqMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FaqDTO insertFaq(FaqDTO dto){
        String memberId = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal()
                            .toString();

        Long userIds = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        boolean exists = memberRepository.existsById(userIds);
        log.info("DB에 회원 존재 여부 (existsById={}): {}", memberId, exists);

        // Member 영속 객체 조회
        // Member member = memberRepository.findByMemberId(dto.getMemberId());
//        Member member = memberRepository.findByMemberId(memberId);
//        log.info("memberId : {}", memberId);
//        if(member == null){
//            throw new RuntimeException("존재하지 않는 회원 입니다!");
//        }

        Optional<Member> optMember = memberRepository.findById(userIds);
        System.out.println("optMember : "+optMember);
        if (optMember.isEmpty()) {
            throw new RuntimeException("존재하지 않는 회원 입니다!");
        }
        Member member = optMember.get();

        Faq faq = toEntity(dto);
        faq.setMember(member);
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

        Faq saveFaq  = faqRepository.save(faq);
        return FaqMapper.toDTO(saveFaq);
        // return toDTO(faqRepository.save(faq));
    }

    public void deleteFaq(int faqNo){
        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(() -> new RuntimeException("해당하는 Faq 를 찾지 못했습니다."));

        faq.setIsVisible("N");
        faq.setFaqDelete(new Date());
        // faqRepository.deleteById(faqNo); // 실제 삭제
    }

    // 페이징된 목록 조회
    public PageResponseDTO<FaqDTO> getListWithPaging(Pageable pageable){
        log.info("페이징된 Faq 목록 조회 - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Faq> faqPage = faqRepository.findByIsVisibleOrderByFaqCreateDateDesc("Y", pageable);

        List<FaqDTO> faqDTOs = faqPage.getContent().stream()
                .map(FaqMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<FaqDTO>builder()
                .content(faqDTOs)
                .currentPage(faqPage.getNumber())
                .pageSize(faqPage.getSize())
                .totalElements(faqPage.getTotalElements())
                .totalPages(faqPage.getTotalPages())
                .first(faqPage.isFirst())
                .last(faqPage.isLast())
                .hasNext(faqPage.hasNext())
                .hasPrevious(faqPage.hasPrevious())
                .build();
    }
}
