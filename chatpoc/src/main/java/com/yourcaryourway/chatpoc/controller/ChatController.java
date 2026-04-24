package com.yourcaryourway.chatpoc.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.yourcaryourway.chatpoc.dto.ChatMessageRequest;
import com.yourcaryourway.chatpoc.dto.MessageDTO;
import com.yourcaryourway.chatpoc.service.MessageService;

@Controller
public class ChatController {
	
	 private final MessageService messageService;
	 private final SimpMessagingTemplate messagingTemplate;
	 
	 
	 
	 
	 
	public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
		super();
		this.messageService = messageService;
		this.messagingTemplate = messagingTemplate;
	}





	@MessageMapping("/send-message")
	public void processMessage(@Payload ChatMessageRequest chatMessage, Principal principal) {
	    
	    String emailAuteur = principal.getName();
	
	    MessageDTO savedMessage = messageService.saveMessage(
	            chatMessage.getConversationId(), 
	            emailAuteur, 
	            chatMessage.getContenu()
	    );
	

	    messagingTemplate.convertAndSend(
	            "/topic/chat/" + chatMessage.getConversationId(), 
	            savedMessage
	    );
	}
	 
	 

}
