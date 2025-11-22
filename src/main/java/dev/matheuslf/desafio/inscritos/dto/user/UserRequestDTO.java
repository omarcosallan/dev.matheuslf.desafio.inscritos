package dev.matheuslf.desafio.inscritos.dto.user;

import dev.matheuslf.desafio.inscritos.annotation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Name cannot be empty or null")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters")
        String name,
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,
        @NotBlank(message = "Password cannot be empty or null")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,
        @ValidRole
        String role
) {
}
