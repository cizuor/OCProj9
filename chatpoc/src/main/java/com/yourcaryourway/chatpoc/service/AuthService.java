package com.yourcaryourway.chatpoc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.dto.AuthResponse;
import com.yourcaryourway.chatpoc.dto.LoginRequest;
import com.yourcaryourway.chatpoc.dto.RegisterRequest;
import com.yourcaryourway.chatpoc.dto.UserDTO;
import com.yourcaryourway.chatpoc.model.Reservation;
import com.yourcaryourway.chatpoc.model.User;
import com.yourcaryourway.chatpoc.model.Voiture;
import com.yourcaryourway.chatpoc.model.enums.Currency;
import com.yourcaryourway.chatpoc.model.enums.Language;
import com.yourcaryourway.chatpoc.repository.ReservationRepository;
import com.yourcaryourway.chatpoc.repository.UserRepository;
import com.yourcaryourway.chatpoc.repository.VoitureRepository;
import com.yourcaryourway.chatpoc.security.jwt.JwtService;

@Service
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VoitureRepository voitureRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final JwtService jwtService;
    
    
    
    
    
    
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService, VoitureRepository voitureRepository,
			ReservationRepository reservationRepository, ReservationService reservationService ) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.voitureRepository = voitureRepository;
		this.reservationRepository = reservationRepository;
		this.reservationService = reservationService;
		this.jwtService = jwtService;
	}






	public AuthResponse register(RegisterRequest request) {
    	
		if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        if (userRepository.existsByPseudo(request.getPseudo())) {
            throw new IllegalArgumentException("Ce pseudo est déjà pris.");
        }
		
    	User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPseudo(request.getPseudo());
        newUser.setLanguage(Language.FR);
        newUser.setCurrency(Currency.EUR);
        
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(newUser);
        reservationService.seedRandomReservationsForUser(savedUser);
        return new AuthResponse(jwtService.generateToken(savedUser));
        
    }
	
	
	
	public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifiant(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailOrPseudo(request.getIdentifiant(), request.getIdentifiant())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
	
	
	

}
