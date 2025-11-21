package dev.matheuslf.desafio.inscritos.dto.user;

import dev.matheuslf.desafio.inscritos.entities.enums.Role;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        Role role
) {
}
