package com.yourcaryourway.chatpoc.dto;

import java.time.LocalDateTime;

import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.model.enums.Currency;
import com.yourcaryourway.chatpoc.model.enums.Language;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String email;
	private String pseudo;
	private Language  language;
	private Currency  currency;
	
	
	
	public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        return new UserDTO(
        		user.getId(),
        		user.getCreatedAt(),
        		user.getUpdatedAt(),
        		user.getEmail(),
        		user.getPseudo(),
        		user.getLanguage(),
        		user.getCurrency()
        );
    }
	
}
