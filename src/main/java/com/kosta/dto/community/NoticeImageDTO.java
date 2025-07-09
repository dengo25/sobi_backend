package com.kosta.dto.community;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeImageDTO {
    private int imageNumber;
    private Integer noticeNo;
    private String fileUrl;
    private LocalDateTime uploadTime;
    private String originalFileName;
    private String fileType;
}
