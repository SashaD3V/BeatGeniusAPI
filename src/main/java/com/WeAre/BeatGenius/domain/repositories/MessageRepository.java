package com.WeAre.BeatGenius.domain.repositories;

import com.WeAre.BeatGenius.domain.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);
    List<Message> findByRecipientId(Long recipientId);
    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}