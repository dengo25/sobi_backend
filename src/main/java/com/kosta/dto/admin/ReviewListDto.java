package com.kosta.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.kosta.dto.review.CategoryDTO;
import com.kosta.dto.review.ReviewImageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDto {

	  private Long tno;
	  
	  private String title;
	  
	  private String content;
	  
	  private Long imageNumber;
	  
	  private LocalDateTime createdAt;
	  
	  private LocalDateTime updatedAt;
	  
	  private String isDeleted;
	  
	  private String confirmed;
	  
	  private String memberId;
	  
	  private Long categoryId;
	  
	  private CategoryDTO category;
	  
	  private List<ReviewImageDTO> images;
}
