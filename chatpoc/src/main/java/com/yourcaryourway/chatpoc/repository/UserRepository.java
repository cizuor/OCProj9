package com.yourcaryourway.chatpoc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcaryourway.chatpoc.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailOrPseudo(String email, String pseudo);
	Optional<User> findByEmail(String email);
	Boolean existsByPseudo(String pseudo);
	Boolean existsByEmail(String email);
}
