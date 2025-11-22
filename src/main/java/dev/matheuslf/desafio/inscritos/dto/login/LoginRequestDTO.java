package dev.matheuslf.desafio.inscritos.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,
        @NotBlank(message = "Password cannot be empty or null")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
