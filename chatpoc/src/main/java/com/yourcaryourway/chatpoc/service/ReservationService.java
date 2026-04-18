package com.yourcaryourway.chatpoc.service;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.ReservationDTO;
import com.yourcaryourway.chatpoc.model.Reservation;
import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.model.Voiture;
import com.yourcaryourway.chatpoc.repository.ReservationRepository;
import com.yourcaryourway.chatpoc.repository.UserRepository;
import com.yourcaryourway.chatpoc.repository.VoitureRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

	private final UserRepository userRepository;
	private final VoitureRepository voitureRepository;
	private final ReservationRepository reservationRepository;
	
	
	
	
	
	
	public ReservationService(UserRepository userRepository, VoitureRepository voitureRepository,
			ReservationRepository reservationRepository) {
		super();
		this.userRepository = userRepository;
		this.voitureRepository = voitureRepository;
		this.reservationRepository = reservationRepository;
	}

	private Reservation  saveReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être nulle");
        }
        return reservationRepository.save(reservation);
    }
	
	@Transactional
	public ReservationDTO createReservation(ReservationDTO request) {
	    Voiture voiture = voitureRepository.findById(request.getVoiture().getId()).orElseThrow(() -> new EntityNotFoundException("Voiture non trouvée"));
	    User user = userRepository.findById(request.getClient().getId()).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
	    long jours = Math.max(1,Duration.between(request.getDateDebut(), request.getDateFin()).toDays());

	    BigDecimal total = voiture.getPrixJournalier().multiply(BigDecimal.valueOf(jours));

	    Reservation resa = new Reservation();
	    resa.setDateDebut(request.getDateDebut());
	    resa.setDateFin(request.getDateFin());
	    resa.setPrixTotal(total);
	    resa.setVoiture(voiture);
	    resa.setClient(user);
	    return ReservationDTO.fromEntity(saveReservation(resa));
	}
}
