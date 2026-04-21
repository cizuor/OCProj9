package com.yourcaryourway.chatpoc.dto;

import java.time.LocalDateTime;

import com.yourcaryourway.chatpoc.model.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
	private Long id;
    private String contenu;
    private String pseudoAuteur;
    private LocalDateTime createdAt;
    
    public static MessageDTO fromEntity(Message message) {
        if (message == null) return null;
        return new MessageDTO(
            message.getId(),
            message.getContenu(),
            message.getAuteur().getPseudo(),
            message.getCreatedAt()
        );
    }
}
