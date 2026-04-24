package com.yourcaryourway.chatpoc.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.MessageDTO;
import com.yourcaryourway.chatpoc.model.Conversation;
import com.yourcaryourway.chatpoc.model.Message;
import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.repository.ConversationRepository;
import com.yourcaryourway.chatpoc.repository.MessageRepository;
import com.yourcaryourway.chatpoc.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class MessageService {
	
	private final ConversationRepository conversationRepository;
	
	private final MessageRepository messageRepository;
	
	private final UserRepository userRepository;

	public MessageService(ConversationRepository conversationRepository, MessageRepository messageRepository,
			UserRepository userRepository) {
		super();
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}
	

	
    public MessageDTO saveMessage(Long conversationId, String userEmail, String contenu) {
        User auteur = userRepository.findByEmailOrPseudo(userEmail,userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation non trouvée"));

        return saveMessage(conv,auteur,contenu);
    }
	
	@Transactional
    public MessageDTO saveMessage(Long conversationId, Long userId, String contenu) {
        User auteur = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation non trouvée"));


        return saveMessage(conv,auteur,contenu);
    }
	
	
	
	@Transactional
    public MessageDTO saveMessage(Conversation conv, User auteur, String contenu) {
        Message message = new Message();
        message.setContenu(contenu);
        message.setAuteur(auteur);
        message.setConversation(conv);

        return MessageDTO.fromEntity(messageRepository.save(message));
    }
	
	
	
	
	
	 public List<MessageDTO> findByConversationId(Long conversationId) {
	        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
	                .stream()
	                .map(MessageDTO::fromEntity)
	                .sorted(Comparator.comparing(MessageDTO::getCreatedAt))
	                .collect(Collectors.toList());
	    }
	
	
}
