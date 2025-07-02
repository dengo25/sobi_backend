package com.kosta.dto.community;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
	
	
//	public static Object builder() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}


