package com.yourcaryourway.chatpoc.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.ReservationDTO;
import com.yourcaryourway.chatpoc.dto.UserDTO;
import com.yourcaryourway.chatpoc.dto.VoitureDTO;
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
	private final Random random = new Random();
	
	
	
	
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
	
	
	public List<ReservationDTO> findByUserEmail(String email){
		
		List<Reservation> reservations;
		if(email.endsWith("chatpoc.com")) {
			reservations = reservationRepository.findAll();
		
		}else {
			reservations = reservationRepository.findByClientEmail(email);
		}
		
		return reservations.stream()
				.map(ReservationDTO::fromEntity)
				.collect(Collectors.toList());
	}
	
	
	
	@Transactional
    public void seedRandomReservationsForUser(User user) {
        List<Voiture> voitures = voitureRepository.findAll();
        if (voitures.isEmpty()) return;

        Collections.shuffle(voitures);
        List<Voiture> selection = voitures.stream().limit(2).toList();

        for (Voiture v : selection) {
            LocalDateTime debut = LocalDateTime.now().plusDays(1 + random.nextInt(30));
            LocalDateTime fin = debut.plusDays(1 + random.nextInt(7));

            ReservationDTO fakeRequest = new ReservationDTO();
            fakeRequest.setDateDebut(debut);
            fakeRequest.setDateFin(fin);
            
            VoitureDTO vDto = new VoitureDTO();
            vDto.setId(v.getId());
            fakeRequest.setVoiture(vDto);

            UserDTO uDto = new UserDTO();
            uDto.setId(user.getId());
            fakeRequest.setClient(uDto);

            this.createReservation(fakeRequest);
        }
    }
}
