package dev.matheuslf.desafio.inscritos.dto.project;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectResponseDTO(
        UUID id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
