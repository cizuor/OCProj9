package com.yourcaryourway.chatpoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yourcaryourway.chatpoc.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private final JwtAuthenticationFilter jwtAuthFilter;
	 private final AuthenticationProvider authenticationProvider;
	 
	 
	 public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
		super();
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
	 }
	    
	 
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(AbstractHttpConfigurer::disable) 
	            .cors(Customizer.withDefaults()) 
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/api/auth/**").permitAll()
	                .requestMatchers("/ws-chat/**").permitAll()  
	                .anyRequest().authenticated()                
	            )
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authenticationProvider(authenticationProvider)
	            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }
	    
}
