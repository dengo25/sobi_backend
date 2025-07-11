package com.kosta.controller.blacklist;

import com.kosta.dto.blacklist.BlacklistRequestDto;
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

    /**
     * 블랙리스트 추가
     */
    @PostMapping
    public ResponseEntity<String> addToBlacklist(@RequestBody BlacklistRequestDto requestDto) {
        try {
            blacklistService.addToBlacklist(requestDto);
            return ResponseEntity.ok("사용자가 블랙리스트에 추가되었습니다.");
            
        } catch (RuntimeException e) {
            log.error("블랙리스트 추가 오류 - 회원: {}, 오류: {}", 
                     requestDto.getMemberId(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 블랙리스트 해제
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> removeFromBlacklist(
            @PathVariable String memberId,
            @RequestParam String reason) {
        try {
            blacklistService.removeFromBlacklist(memberId, reason);
            return ResponseEntity.ok("블랙리스트가 해제되었습니다.");
            
        } catch (RuntimeException e) {
            log.error("블랙리스트 해제 오류 - 회원: {}, 오류: {}", memberId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 블랙리스트 상태 확인
     */
    @GetMapping("/check/{memberId}")
    public ResponseEntity<Boolean> checkBlacklistStatus(@PathVariable String memberId) {
        boolean isBlacklisted = blacklistService.isBlacklisted(memberId);
        return ResponseEntity.ok(isBlacklisted);
    }
}