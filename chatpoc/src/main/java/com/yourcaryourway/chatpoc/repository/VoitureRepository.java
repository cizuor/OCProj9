package com.yourcaryourway.chatpoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcaryourway.chatpoc.model.Voiture;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {

}
