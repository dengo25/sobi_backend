package com.kosta.util;

import lombok.Data;

import java.util.List;

@Data
public class PresignedUrlRequestDTO {

 
  private String folder;
  private List<String> filenames;
}
