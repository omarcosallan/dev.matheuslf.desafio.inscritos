package dev.matheuslf.desafio.inscritos.dto.project;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectRequestDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @Size(max = 255)
        String description,
        @NotNull
        @FutureOrPresent
        LocalDate startDate,
        @Future
        LocalDate endDate,
        UUID ownerId
) {
}
