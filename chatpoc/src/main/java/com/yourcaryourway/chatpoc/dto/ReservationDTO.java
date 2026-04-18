package com.yourcaryourway.chatpoc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.yourcaryourway.chatpoc.model.Reservation;
import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.model.Voiture;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationDTO {
	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private BigDecimal prixTotal;
	private UserDTO client;
	private VoitureDTO voiture;
	
	public static ReservationDTO fromEntity(Reservation reservation) {
		if(reservation == null) return null;
		
		UserDTO user = UserDTO.fromEntity(reservation.getClient());
		VoitureDTO voiture = VoitureDTO.fromEntity(reservation.getVoiture()); 
		return new ReservationDTO(
				reservation.getId(),
				reservation.getCreatedAt(),
				reservation.getUpdatedAt(), 
				reservation.getDateDebut(),
				reservation.getDateFin(),
				reservation.getPrixTotal(),
				user, 
				voiture);
	
	}
	
	
}
