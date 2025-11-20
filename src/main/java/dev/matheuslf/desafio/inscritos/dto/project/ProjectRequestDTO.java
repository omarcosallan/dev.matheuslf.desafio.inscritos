package dev.matheuslf.desafio.inscritos.dto.project;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProjectRequestDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @Size(max = 255)
        String description,
        @FutureOrPresent
        LocalDate startDate,
        @Future
        LocalDate endDate
) {
}
