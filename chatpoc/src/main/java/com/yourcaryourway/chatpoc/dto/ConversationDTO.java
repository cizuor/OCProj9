package com.yourcaryourway.chatpoc.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.yourcaryourway.chatpoc.model.Conversation;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ConversationDTO {

	private Long id;
	private Long reservationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	private List<MessageDTO> messages = new ArrayList<>();
	
	
	public static ConversationDTO fromEntity(Conversation conversation) {
		if (conversation == null) {
			return null;
		}
		
		
		List<MessageDTO> messageDTOs = new ArrayList<>();
		
		
		messageDTOs = conversation.getMessages().stream()
	            .map(MessageDTO::fromEntity)
	            .sorted(Comparator.comparing(MessageDTO::getCreatedAt))
	            .collect(Collectors.toList());
		
		return new ConversationDTO(
				conversation.getId(),
				conversation.getReservation().getId(),
				conversation.getCreatedAt(),
				conversation.getUpdatedAt(),
				messageDTOs
				);
	}

	
	
	
}
