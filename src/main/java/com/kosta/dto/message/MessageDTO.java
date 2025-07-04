package com.kosta.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    
    private Integer id;
    private Long senderId;
    private String senderName;
    private String senderMemberId; // 보내는 사람의 memberId
    private Long receiverId;
    private String receiverName;
    private String receiverMemberId; // 받는 사람의 memberId
    private String title;
    private String content;
    private LocalDateTime sendDate;
    private String isRead;
    private String deletedBySender;
    private String deletedByReceiver;
}