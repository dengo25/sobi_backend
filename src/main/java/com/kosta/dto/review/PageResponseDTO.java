package com.kosta.dto.review;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
  
  private List<E> rnoList;
  
  private List<Integer> pageNumList;
  
  private PageRequestDTO pageRequestDTO;
  
  private boolean prev,next; //화살표로 표현된는 <>
  
  private int totalCount, prevPage, nextPage, totalPage, current;
  
  @Builder(builderMethodName = "withAll")
  public PageResponseDTO(List<E> rnoList, PageRequestDTO pageRequestDTO, long totalCount) {
    
    this.rnoList = rnoList;
    this.pageRequestDTO = pageRequestDTO;
    this.totalCount = (int) totalCount;
    
    //끝페이지 end부터 계산
    int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10; //현재페이지를 10.0으로 나누고 올림한 뒤에 곱하기 10
    
    //시작 페이지
    int start = end - 9;
    
    //진짜 마지막 페이지
    // 소수 점으로 토탈을 나누고 올림 처리 후 10으로 나눔
    int last = (int) Math.ceil(totalCount / (double) pageRequestDTO.getSize());
    
    end = end > last ? last : end; // 마지막 페이지가 8페이지인데 계신이 10으로 나온다면?
    
    this.prev = start > 1;
    this.next = totalCount > end * pageRequestDTO.getSize(); //totalCount가 101이고 글의 개수가 100이면 next 버튼이 생겨야
    
    //페이지네이션 하단에 나올 페이지 번호 리스트를 만든다.
    this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    
    this.prevPage = prev ? start - 1 : 0;
    
    this.nextPage = next ? end + 1 : 0;
  }
}
