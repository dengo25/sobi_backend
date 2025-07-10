package com.kosta.dto.community;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {
    private int noticeNo;
    private String memberId;
    private String noticeTitle;
    private String noticeContent;
    private int count;
    private LocalDateTime noticeCreateDate;
    private LocalDateTime noticeEditDate;
    private LocalDateTime noticeDeleteDate;
    private String isDeleted;
    private String isVisible;
    private Integer noticeImageNumber;
    private List<String> imageUrls; // presigned 업로드 후 받아온 이미지 URL 목록
}
