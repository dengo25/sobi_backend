package com.kosta.service.message;

import com.kosta.domain.member.Member;
import com.kosta.domain.message.Message;
import com.kosta.dto.message.MessageDTO;
import com.kosta.repository.member.MemberRepository;
import com.kosta.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    
    // 쪽지 전송
    public MessageDTO sendMessage(MessageDTO messageDTO, Long senderId) {
        log.info("쪽지 전송 요청: senderId={}, receiverMemberId={}", senderId, messageDTO.getReceiverMemberId());
        
        // 발신자 조회
        Member sender = memberRepository.findByIdAndIsActive(senderId, "Y")
                .orElseThrow(() -> new RuntimeException("발신자를 찾을 수 없습니다."));
        
        // 수신자 조회 (memberId로 검색)
        Member receiver = memberRepository.findByMemberIdAndIsActive(messageDTO.getReceiverMemberId(), "Y");
        if (receiver == null) {
            throw new RuntimeException("수신자를 찾을 수 없습니다: " + messageDTO.getReceiverMemberId());
        }
        
        // 자기 자신에게 보내기 관련
        // if (sender.getId().equals(receiver.getId())) {
        //     throw new RuntimeException("자기 자신에게는 쪽지를 보낼 수 없습니다.");
        // }
        
        // 메시지 생성
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .title(messageDTO.getTitle())
                .content(messageDTO.getContent())
                .build();
        
        Message savedMessage = messageRepository.save(message);
        log.info("쪽지 전송 완료: messageId={}, 발신자={}, 수신자={}", 
                savedMessage.getId(), sender.getMemberId(), receiver.getMemberId());
        
        return convertToDTO(savedMessage);
    }
    
    // 받은 쪽지 목록 조회
    public List<MessageDTO> getReceivedMessages(Long userId) {
        Member user = memberRepository.findByIdAndIsActive(userId, "Y")
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        List<Message> messages = messageRepository.findByReceiverAndDeletedByReceiverOrderBySendDateDesc(user, "N");
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // 쪽지 읽음 처리
    public void markAsRead(Integer messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("쪽지를 찾을 수 없습니다."));
        
        // 수신자만 읽음 처리 가능
        if (!message.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("읽음 처리 권한이 없습니다.");
        }
        
        message.setIsRead("Y");
        messageRepository.save(message);
        log.info("쪽지 읽음 처리: messageId={}", messageId);
    }
    
    // 쪽지 삭제 (수신자용)
    public void deleteMessageByReceiver(Integer messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("쪽지를 찾을 수 없습니다."));
        
        if (!message.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        message.setDeletedByReceiver("Y");
        messageRepository.save(message);
        log.info("수신자 쪽지 삭제: messageId={}", messageId);
    }
    
    // 읽지 않은 쪽지 개수
    public long getUnreadMessageCount(Long userId) {
        Member user = memberRepository.findByIdAndIsActive(userId, "Y")
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return messageRepository.countUnreadMessages(user);
    }
    
    // Entity -> DTO 변환
    private MessageDTO convertToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getMemberName())
                .senderMemberId(message.getSender().getMemberId())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getMemberName())
                .receiverMemberId(message.getReceiver().getMemberId())
                .title(message.getTitle())
                .content(message.getContent())
                .sendDate(message.getSendDate())
                .isRead(message.getIsRead())
                .deletedBySender(message.getDeletedBySender())
                .deletedByReceiver(message.getDeletedByReceiver())
                .build();
    }
}