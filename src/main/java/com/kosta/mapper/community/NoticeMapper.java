package com.kosta.mapper.community;

import com.kosta.domain.community.Notice;
import com.kosta.domain.member.Member;
import com.kosta.dto.common.PageResponseDTO;
import com.kosta.dto.community.NoticeDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class NoticeMapper {

    // Date -> LocalDateTime
    private static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // LocalDateTime -> Date
    private static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Entity -> DTO
    public static NoticeDTO toDTO(Notice notice){
        if(notice == null) return null;
        return NoticeDTO.builder()
                .noticeNo(notice.getNoticeNo())
                .memberId(notice.getMember() != null ? notice.getMember().getMemberId() : null)
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .count(notice.getCount())
                .noticeCreateDate(notice.getNoticeCreateDate())
                .noticeEditDate(notice.getNoticeEditDate())
                .noticeDeleteDate(notice.getNoticeDeleteDate())
                .isDeleted(notice.getIsDeleted())
                .isVisible(notice.getIsVisible())
                .noticeImageNumber(notice.getNoticeImageNumber())
                .build();
    }

    // DTO -> Entity
    public static Notice toEntity(NoticeDTO dto){
        if(dto == null) return null;

        Notice notice = new Notice();
        notice.setNoticeNo(dto.getNoticeNo());
        notice.setNoticeTitle(dto.getNoticeTitle());
        notice.setNoticeContent(dto.getNoticeContent());
        notice.setCount(dto.getCount());
        notice.setNoticeCreateDate(dto.getNoticeCreateDate());
        notice.setNoticeEditDate(dto.getNoticeEditDate());
        notice.setNoticeDeleteDate(dto.getNoticeDeleteDate());
        notice.setIsDeleted(dto.getIsDeleted());
        notice.setIsVisible(dto.getIsVisible());
        notice.setNoticeImageNumber(dto.getNoticeImageNumber());

        if(dto.getMemberId() != null && !dto.getMemberId().isEmpty()){
            Member member = new Member();
            member.setMemberId(dto.getMemberId());
            notice.setMember(member);
        }
        return notice;
    }

}
