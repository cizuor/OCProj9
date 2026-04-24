package com.yourcaryourway.chatpoc.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
	 private String contenu;
	 private Long conversationId;
}
