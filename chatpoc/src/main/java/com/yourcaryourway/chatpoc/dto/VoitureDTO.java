package com.yourcaryourway.chatpoc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.yourcaryourway.chatpoc.model.Voiture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoitureDTO {
	private Long id;
	private String nom;
	private BigDecimal prixJournalier; 
	
	
	public static VoitureDTO fromEntity(Voiture voiture) {
        if (voiture == null) return null;
        return new VoitureDTO(
            voiture.getId(),
            voiture.getNom(),
            voiture.getPrixJournalier()
        );
    }
}
