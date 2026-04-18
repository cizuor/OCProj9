package com.yourcaryourway.chatpoc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Voiture")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Voiture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String nom;
	
	@NotNull
    @Column(precision = 10, scale = 2) // Ex: 99999999.99
    private BigDecimal prixJournalier; 

}
