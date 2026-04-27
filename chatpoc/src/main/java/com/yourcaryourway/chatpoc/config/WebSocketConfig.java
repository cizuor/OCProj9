package com.yourcaryourway.chatpoc.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.yourcaryourway.chatpoc.security.jwt.JwtService;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	
	private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    
    
    
    
	public WebSocketConfig(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
	
	    config.enableSimpleBroker("/topic");
	
	    config.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
	    registry.addEndpoint("/ws-chat")
	        .setAllowedOrigins("http://localhost:4200");
	}
	    
	    
	    
	    
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
	    registration.interceptors(new ChannelInterceptor() {
	        @Override
	        public Message<?> preSend(Message<?> message, MessageChannel channel) {
	            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
	
	            // Si c'est une tentative de connexion, on vérifie le badge (Token)
	            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
	                String authHeader = accessor.getFirstNativeHeader("Authorization");
	                
	                if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                    String token = authHeader.substring(7);
	                    String userEmail = jwtService.extractUsername(token);
	                    
	                    if (userEmail != null) {
	                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
	                        
	                        if (jwtService.isTokenValid(token, userDetails)) {
	                            // On crée l'identité officielle
	                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	                                    userDetails, null, userDetails.getAuthorities());
	                            
	                            // ON INJECTE L'IDENTITÉ DANS LE WEBSOCKET
	                            accessor.setUser(auth);
	                        }
	                    }
	                }
	            }
	            return message;
	        }
	    });
	}
	    
	    
	    
}
