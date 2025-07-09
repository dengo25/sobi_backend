package com.kosta.domain.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "notice")
@Builder
public class NoticeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_NUMBER")
    private int imageNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_NO", referencedColumnName = "NOTICE_NO")
    private Notice notice;

    @Column(name = "FILE_URL", length = 300)
    private String fileUrl;

    @Column(name = "UPLOAD_TIME")
    private LocalDateTime uploadTime;

    @Column(name = "ORIGINAL_FILE_NAME", length = 1000)
    private String originalFileName;

    @Column(name = "FILE_TYPE", length = 100)
    private String fileType;
}