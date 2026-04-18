package com.yourcaryourway.chatpoc.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
    @Size(max = 3000)
    @Column(nullable = false, columnDefinition  = "TEXT")
    private String contenu;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User auteur;
	
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Conversation conversation;
	
	@CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;
	
	
	
}
