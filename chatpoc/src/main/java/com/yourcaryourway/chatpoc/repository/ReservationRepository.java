package com.yourcaryourway.chatpoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcaryourway.chatpoc.model.Reservation;
import com.yourcaryourway.chatpoc.model.Conversation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	 List<Reservation> findAllByClientId(Long userId);
	 Optional<Conversation> findByReservationId(Long reservationId);
}
