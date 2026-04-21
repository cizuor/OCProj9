package com.yourcaryourway.chatpoc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourcaryourway.chatpoc.dto.ConversationDTO;
import com.yourcaryourway.chatpoc.service.ConversationService;

@RestController
@RequestMapping("/api/conversation")
public class ConversationController {
	
	private final ConversationService conversationService;

	public ConversationController(ConversationService conversationService) {
		super();
		this.conversationService = conversationService;
	}
	
	
	@GetMapping("/me")
    public ResponseEntity<List<ConversationDTO>> getMyConversations(Principal principal) {
        return ResponseEntity.ok(conversationService.findByUserEmail(principal.getName()));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ConversationDTO> getConversationByReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(conversationService.getOrCreateConversation(reservationId));
    }
	

}
