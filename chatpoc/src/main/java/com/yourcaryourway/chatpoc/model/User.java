package com.yourcaryourway.chatpoc.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yourcaryourway.chatpoc.model.enums.Currency;
import com.yourcaryourway.chatpoc.model.enums.Language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
	    name = "users",
	    uniqueConstraints = @UniqueConstraint(columnNames = "email")
	)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;
	
	@UpdateTimestamp
    private LocalDateTime updatedAt;
	
	
	@NotBlank
    @Email
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String email;
	
	@NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String pseudo;
	
	@NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 2, nullable = false)
	private Language  language = Language.FR;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 3, nullable = false)
	private Currency currency = Currency.EUR;
	
	
	 @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getUsername() {
		return email;
	}
	
}
