package com.kosta.domain.community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kosta.domain.member.Member;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "noticeImages"})
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_NO")
    private int noticeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
    private Member member;

    @Column(name = "NOTICE_TITLE", length = 200)
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT", columnDefinition = "TEXT")
    private String noticeContent;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "NOTICE_CREATE_DATE")
    private LocalDateTime noticeCreateDate;

    @Column(name = "NOTICE_EDIT_DATE")
    private LocalDateTime noticeEditDate;

    @Column(name = "NOTICE_DELETE_DATE")
    private LocalDateTime noticeDeleteDate;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

    @Column(name = "IS_VISIBLE", length = 1)
    private String isVisible;

    @Column(name = "NOTICE_IMAGE_NUMBER")
    private Integer noticeImageNumber;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeImage> noticeImages = new ArrayList<>();
}