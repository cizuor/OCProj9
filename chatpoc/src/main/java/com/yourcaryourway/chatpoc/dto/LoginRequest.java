package com.yourcaryourway.chatpoc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message = "L'identifiant (email ou pseudo) est obligatoire")
    private String identifiant;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
