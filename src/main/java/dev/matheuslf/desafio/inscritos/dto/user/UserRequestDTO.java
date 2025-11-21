package dev.matheuslf.desafio.inscritos.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @Email
        @Size(max = 150)
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password,
        String role
) {
}
