package com.kosta.repository.message;

import com.kosta.domain.message.Message;
import com.kosta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    // 받은 쪽지 조회 (삭제되지 않은 것만)
    List<Message> findByReceiverAndDeletedByReceiverOrderBySendDateDesc(Member receiver, String deletedByReceiver);
    
    // 보낸 쪽지 조회 (삭제되지 않은 것만)
    List<Message> findBySenderAndDeletedBySenderOrderBySendDateDesc(Member sender, String deletedBySender);
    
    // 받은 쪽지 중 읽지 않은 쪽지 개수
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :receiver AND m.isRead = 'N' AND m.deletedByReceiver = 'N'")
    long countUnreadMessages(@Param("receiver") Member receiver);
    
    // 특정 사용자와의 대화 조회
    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender = :user1 AND m.receiver = :user2 AND m.deletedBySender = 'N') OR " +
           "(m.sender = :user2 AND m.receiver = :user1 AND m.deletedByReceiver = 'N')) " +
           "ORDER BY m.sendDate ASC")
    List<Message> findConversationBetween(@Param("user1") Member user1, @Param("user2") Member user2);
}