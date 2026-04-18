package com.yourcaryourway.chatpoc.security.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.yourcaryourway.chatpoc.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	 private static final String SECRET_KEY = "YourCarYourWaySecretKeyForJwtAuthenticationPoC";

	    public String generateToken(User userDetails) {
	        return Jwts.builder()
	                .subject(userDetails.getUsername())
	                .issuedAt(new Date(System.currentTimeMillis()))
	                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expire dans 24h
	                .signWith(getSigningKey())
	                .compact();
	    }

	    public String extractUsername(String token) {
	    	 return extractClaim(token, Claims::getSubject);
	    }

	    public boolean isTokenValid(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	    }

	    private boolean isTokenExpired(String token) {
	    	return extractClaim(token, Claims::getExpiration).before(new Date());
	    }

	    private SecretKey getSigningKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
	    
	    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = Jwts.parser()
	                .verifyWith(getSigningKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	        return claimsResolver.apply(claims);
	    }
}
