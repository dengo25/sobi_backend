package com.kosta.dto.community;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqDTO {
	private	int faqNo;
	private String memberId;
	private	String faqCategory;
	private	String faqQuestion;
	private	String faqAnswer;
	private	Date faqCreateDate;
	private	Date faqEditDate;
	private	Date faqDelete;
	private	String isDeleted;
	private	String isVisible;
}


