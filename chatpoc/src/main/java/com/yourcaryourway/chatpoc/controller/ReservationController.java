package com.yourcaryourway.chatpoc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourcaryourway.chatpoc.dto.ReservationDTO;
import com.yourcaryourway.chatpoc.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		super();
		this.reservationService = reservationService;
	}
	@GetMapping("/me")
	public ResponseEntity<List<ReservationDTO>> getMyReservation(Principal principal){
		return ResponseEntity.ok(reservationService.findByUserEmail(principal.getName()));
	}

}
