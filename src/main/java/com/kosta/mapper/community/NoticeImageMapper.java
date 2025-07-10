package com.kosta.mapper.community;

import com.kosta.domain.community.Notice;
import com.kosta.domain.community.NoticeImage;
import com.kosta.dto.community.NoticeImageDTO;

// 미사용시 삭제 예정
public class NoticeImageMapper {
    // Entity -> DTO
    public static NoticeImageDTO toDTO(NoticeImage noticeImage){
        if(noticeImage == null) return null;
        return NoticeImageDTO.builder()
                .imageNumber(noticeImage.getImageNumber())
                .noticeNo(noticeImage.getNotice() != null ? noticeImage.getNotice().getNoticeNo() : null)
                .fileUrl(noticeImage.getFileUrl())
                .uploadTime(noticeImage.getUploadTime())
                .originalFileName(noticeImage.getOriginalFileName())
                .fileType(noticeImage.getFileType())
                .build();
    }

    // DTO -> Entity
    public static NoticeImage toEntity(NoticeImageDTO dto){
        if(dto == null) return null;

        NoticeImage noticeImage = new NoticeImage();
        noticeImage.setImageNumber(dto.getImageNumber());
        noticeImage.setFileUrl(dto.getFileUrl());
        noticeImage.setUploadTime(dto.getUploadTime());
        noticeImage.setOriginalFileName(dto.getOriginalFileName());
        noticeImage.setFileType(dto.getFileType());

        if(dto.getNoticeNo() != null){
            Notice notice = new Notice();
            notice.setNoticeNo(dto.getNoticeNo());
            noticeImage.setNotice(notice);
        }
        return noticeImage;
    }
}
