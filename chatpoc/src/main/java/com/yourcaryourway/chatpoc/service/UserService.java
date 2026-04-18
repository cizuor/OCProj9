package com.yourcaryourway.chatpoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.ReservationDTO;
import com.yourcaryourway.chatpoc.dto.UserDTO;
import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.repository.ReservationRepository;
import com.yourcaryourway.chatpoc.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	
	 private final UserRepository userRepository;
	 private final ReservationRepository reservationRepository;
	 
	 
	 public UserService(UserRepository userRepository, ReservationRepository reservationRepository) {
		super();
		this.userRepository = userRepository;
		this.reservationRepository = reservationRepository;
	 }
	    
	 
	 public UserDTO findById(Long userId) {
		 return UserDTO.fromEntity(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé")));
	 }
	    
	 public List<ReservationDTO> getUserReservations(Long userId) {
	        return reservationRepository.findAllByClientId(userId)
	                .stream()
	                .map(ReservationDTO::fromEntity)
	                .collect(Collectors.toList());
	    }
	 
	 public User save(User user) {
	        return userRepository.save(user);
	    }
}
