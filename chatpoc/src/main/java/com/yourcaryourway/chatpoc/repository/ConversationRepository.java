package com.yourcaryourway.chatpoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcaryourway.chatpoc.model.Conversation;

@Repository
public interface ConversationRepository  extends JpaRepository<Conversation, Long> {
	 Optional<Conversation> findByReservationId(Long reservationId);
	 List<Conversation> findByReservationClientId(Long userId);
}
