package dev.matheuslf.desafio.inscritos.dto.project;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProjectRequestDTO(
        @NotBlank(message = "Name cannot be empty or null")
        @Size(min = 3, max = 100, message = "Name cannot be shorter than 3 characters or longer than 100 characters")
        String name,
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,
        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be in the present or in the future")
        LocalDate startDate,
        @Future(message = "End date must be in the present")
        LocalDate endDate,
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String ownerEmail
) {
}
