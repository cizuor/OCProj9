package com.yourcaryourway.chatpoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.ConversationDTO;
import com.yourcaryourway.chatpoc.model.Conversation;
import com.yourcaryourway.chatpoc.model.Reservation;
import com.yourcaryourway.chatpoc.repository.ConversationRepository;
import com.yourcaryourway.chatpoc.repository.MessageRepository;
import com.yourcaryourway.chatpoc.repository.ReservationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ConversationService {
	
	
	private final ConversationRepository conversationRepository;
	
	private final MessageRepository messageRepository;
	
	private final ReservationRepository reservationRepository;

	public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository, ReservationRepository reservationRepository) {
		super();
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
		this.reservationRepository = reservationRepository;
		
	}
	
	
	
	public ConversationDTO findByID(Long id) {
		return ConversationDTO.fromEntity(conversationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conversation non trouvé")));
	}
	
	@Transactional
	public ConversationDTO findByIdReservation (Long reservationId) {
		
		
		return conversationRepository.findByReservationId(reservationId)
                .map(ConversationDTO::fromEntity)
                .orElseGet(() -> {
                    return createAndSaveNewConversation(reservationId);
                });
	}
	
	
	 private ConversationDTO createAndSaveNewConversation(Long reservationId) {
	        Reservation reservation = reservationRepository.findById(reservationId)
	                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée"));

	        Conversation newConv = new Conversation();
	        newConv.setReservation(reservation);
	        
	        Conversation savedConv = SaveConversation(newConv);
	        
	        return ConversationDTO.fromEntity(savedConv);
	    }
	 
	 
	 
	 private Conversation SaveConversation(Conversation conv) {
		 if (conv == null) {
		        throw new IllegalArgumentException("L'objet Conversation ne peut pas être nul");
		    }
		 
		 if (conv.getReservation() == null) {
		        throw new IllegalStateException("Impossible de sauvegarder une conversation sans réservation associée");
		    }
		 
		 
		 return conversationRepository.save(conv);
		 
	 }
	 
	 
	 public List<ConversationDTO> findByUserID(Long userId){
		 List<Conversation> conversations = conversationRepository.findByReservationClientId(userId);
		 
		 return conversations.stream()
		            .map(ConversationDTO::fromEntity) 
		            .collect(Collectors.toList());
	 }
	
	
	

}
