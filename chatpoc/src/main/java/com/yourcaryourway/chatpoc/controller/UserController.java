package com.yourcaryourway.chatpoc.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourcaryourway.chatpoc.dto.UserDTO;
import com.yourcaryourway.chatpoc.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
	    // Principal contient l'email extrait du JWT automatiquement par Spring !
	    return ResponseEntity.ok(userService.FindByEmailOrPseudo(principal.getName()));
	}

}
