package com.kosta.domain.community;

import java.util.Date;

import com.kosta.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "faq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_NO")
    private int faqNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
    private Member member;

    @Column(name = "FAQ_CATEGORY", length = 20)
    private String faqCategory;

    @Column(name = "FAQ_QUESTION", length = 50)
    private String faqQuestion;

    @Column(name = "FAQ_ANSWER", length = 100)
    private String faqAnswer;

    @Column(name = "FAQ_CREATE_DATE")
    private Date faqCreateDate;

    @Column(name = "FAQ_EDIT_DATE")
    private Date faqEditDate;

    @Column(name = "FAQ_DELETE")
    private Date faqDelete;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

    @Column(name = "IS_VISIBLE", length = 1)
    private String isVisible;

}