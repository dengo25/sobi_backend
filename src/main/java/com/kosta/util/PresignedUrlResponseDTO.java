package com.kosta.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponseDTO {
  
  private String filename;
  private String presignedUrl;
  
}
