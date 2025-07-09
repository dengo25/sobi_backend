package com.kosta.service.common;

import com.kosta.dto.community.NoticeDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class PaginationService {
//    public PageResponseDTO<NoticeDTO> getNoticeList(String keyword, int page, int size) {
//            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
//            Page<Notice> noticePage = noticeRepository.findByTitleContaining(keyword, pageable);
//
//            List<NoticeDTO> content = noticePage.getContent().stream()
//                .map(NoticeDTO::fromEntity)
//                .toList();
//
//        return new PageResponseDTO<>(
//                content,
//                noticePage.getNumber() + 1,
//                noticePage.getSize(),
//                noticePage.getTotalElements(),
//                noticePage.getTotalPages(),
//                noticePage.hasNext(),
//                noticePage.hasPrevious()
//        );
//    }


}
