package com.kosta.service.community;

import com.kosta.domain.community.Notice;
import com.kosta.dto.community.FaqDTO;
import com.kosta.dto.community.NoticeDTO;

import java.util.List;

public interface NoticeService {
    List<NoticeDTO> getAllNotice();

    NoticeDTO getDetailNotice(int noticeNo);

    NoticeDTO insertNotice(NoticeDTO dto);

    NoticeDTO updateNotice(NoticeDTO dto);

    NoticeDTO deleteNotice(int noticeNo);
}
