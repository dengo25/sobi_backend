package com.kosta.controller.message;

import com.kosta.dto.member.ResponseDTO;
import com.kosta.dto.message.MessageDTO;
import com.kosta.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	// 쪽지 전송
	@PostMapping("/send")
	public ResponseEntity<?> sendMessage(@AuthenticationPrincipal String userId, @RequestBody MessageDTO messageDTO) {
		try {
			log.info("쪽지 전송 요청: userId={}, messageDTO={}", userId, messageDTO);

			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			MessageDTO sentMessage = messageService.sendMessage(messageDTO, Long.parseLong(userId));
			return ResponseEntity.ok().body(sentMessage);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("쪽지 전송 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 받은 쪽지 목록 조회
	@GetMapping("/received")
	public ResponseEntity<?> getReceivedMessages(@AuthenticationPrincipal String userId) {
		try {
			log.info("받은 쪽지 목록 조회: userId={}", userId);

			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			List<MessageDTO> messages = messageService.getReceivedMessages(Long.parseLong(userId));
			return ResponseEntity.ok().body(messages);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("받은 쪽지 조회 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 보낸 쪽지 목록 조회
	@GetMapping("/sent")
	public ResponseEntity<?> getSentMessages(@AuthenticationPrincipal String userId) {
		try {
			log.info("보낸 쪽지 목록 조회: userId={}", userId);

			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			List<MessageDTO> messages = messageService.getSentMessages(Long.parseLong(userId));
			return ResponseEntity.ok().body(messages);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("보낸 쪽지 조회 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 쪽지 읽음 처리
	@PatchMapping("/{messageId}/read")
	public ResponseEntity<?> markAsRead(@AuthenticationPrincipal String userId,
			@PathVariable("messageId") Integer messageId) {
		log.info("=== 쪽지 읽음 처리 컨트롤러 시작 ===");
		log.info("Request URI: /api/messages/{}/read", messageId);
		log.info("HTTP Method: PATCH");
		log.info("userId from @AuthenticationPrincipal: {}", userId);
		log.info("messageId from @PathVariable: {}", messageId);

		try {
			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			if (messageId == null) {
				log.error("messageId가 null");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("쪽지 ID가 필요합니다.").build());
			}

			log.info("서비스 호출 전 - userId: {}, messageId: {}", userId, messageId);
			messageService.markAsRead(messageId, Long.parseLong(userId));
			log.info("서비스 호출 완료");

			ResponseDTO responseDTO = ResponseDTO.builder().error(null).build();
			log.info("응답 반환: {}", responseDTO);
			return ResponseEntity.ok().body(responseDTO);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("쪽지 읽음 처리 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 읽지 않은 쪽지 개수 조회
	@GetMapping("/unread-count")
	public ResponseEntity<?> getUnreadMessageCount(@AuthenticationPrincipal String userId) {
		try {
			log.info("읽지 않은 쪽지 개수 조회: userId={}", userId);

			if (userId == null || userId.trim().isEmpty()) {
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			long count = messageService.getUnreadMessageCount(Long.parseLong(userId));
			return ResponseEntity.ok().body(count);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("읽지 않은 쪽지 개수 조회 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 쪽지 삭제 (수신자용)
	@DeleteMapping("/{messageId}/receiver")
	public ResponseEntity<?> deleteMessageByReceiver(@AuthenticationPrincipal String userId,
			@PathVariable("messageId") Integer messageId) {
		log.info("=== 수신자 쪽지 삭제 컨트롤러 시작 ===");
		log.info("Request URI: /api/messages/{}/receiver", messageId);
		log.info("HTTP Method: DELETE");
		log.info("userId from @AuthenticationPrincipal: {}", userId);
		log.info("messageId from @PathVariable: {}", messageId);

		try {
			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			if (messageId == null) {
				log.error("messageId가 null");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("쪽지 ID가 필요합니다.").build());
			}

			log.info("서비스 호출 전 - userId: {}, messageId: {}", userId, messageId);
			messageService.deleteMessageByReceiver(messageId, Long.parseLong(userId));
			log.info("서비스 호출 완료");

			ResponseDTO responseDTO = ResponseDTO.builder().error(null).build();
			log.info("응답 반환: {}", responseDTO);
			return ResponseEntity.ok().body(responseDTO);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("수신자 쪽지 삭제 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 쪽지 삭제 (발신자용)
	@DeleteMapping("/{messageId}/sender")
	public ResponseEntity<?> deleteMessageBySender(@AuthenticationPrincipal String userId,
			@PathVariable("messageId") Integer messageId) {
		log.info("=== 발신자 쪽지 삭제 컨트롤러 시작 ===");
		log.info("Request URI: /api/messages/{}/sender", messageId);
		log.info("HTTP Method: DELETE");
		log.info("userId from @AuthenticationPrincipal: {}", userId);
		log.info("messageId from @PathVariable: {}", messageId);

		try {
			if (userId == null || userId.trim().isEmpty()) {
				log.error("userId가 null이거나 비어있음");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("사용자 인증 정보가 없습니다.").build());
			}

			if (messageId == null) {
				log.error("messageId가 null");
				return ResponseEntity.badRequest().body(ResponseDTO.builder().error("쪽지 ID가 필요합니다.").build());
			}

			log.info("서비스 호출 전 - userId: {}, messageId: {}", userId, messageId);
			messageService.deleteMessageBySender(messageId, Long.parseLong(userId));
			log.info("서비스 호출 완료");

			ResponseDTO responseDTO = ResponseDTO.builder().error(null).build();
			log.info("응답 반환: {}", responseDTO);
			return ResponseEntity.ok().body(responseDTO);

		} catch (NumberFormatException e) {
			log.error("userId 파싱 오류: {}", userId, e);
			return ResponseEntity.badRequest().body(ResponseDTO.builder().error("잘못된 사용자 정보입니다.").build());
		} catch (Exception e) {
			log.error("발신자 쪽지 삭제 오류: ", e);
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}