package com.kosta.controller.blacklist;

import com.kosta.dto.blacklist.UnblockRequestDto;
import com.kosta.service.blacklist.BlacklistService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin/blacklist")
@RequiredArgsConstructor
@Slf4j
public class BlacklistController {

    private final BlacklistService blacklistService;


    @PostMapping("/unblock/{blacklistNo}")
    public ResponseEntity<String> unblockUser(@PathVariable int blacklistNo, @RequestBody UnblockRequestDto requestDto){
    	try {
    		blacklistService.removeFromBlacklist(blacklistNo, requestDto.getDetail());
    		return ResponseEntity.ok("차단 해제가 완료되었습니다.");
    	}catch (Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
}