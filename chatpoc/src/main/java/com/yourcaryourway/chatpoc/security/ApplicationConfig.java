package com.yourcaryourway.chatpoc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yourcaryourway.chatpoc.repository.UserRepository;

@Configuration
public class ApplicationConfig {
	
	private final UserRepository userRepository;

	public ApplicationConfig(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Bean
    public UserDetailsService userDetailsService() {

        return identifiant -> userRepository.findByEmailOrPseudo(identifiant,identifiant)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec identifiant : " + identifiant));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
