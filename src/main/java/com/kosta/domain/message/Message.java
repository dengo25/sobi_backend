package com.kosta.domain.message;

import com.kosta.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;
    
    @Column(name = "message_title", length = 100)
    private String title;
    
    @Column(name = "message_content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "message_send_date")
    private LocalDateTime sendDate;
    
    @Builder.Default
    @Column(name = "message_is_read", length = 1)
    private String isRead = "N";
    
    @Builder.Default
    @Column(name = "deleted_by_sender", length = 1)
    private String deletedBySender = "N";
    
    @Builder.Default
    @Column(name = "deleted_by_receiver", length = 1)
    private String deletedByReceiver = "N";
    
    @PrePersist
    protected void onCreate() {
        if (sendDate == null) {
            sendDate = LocalDateTime.now();
        }
    }
}