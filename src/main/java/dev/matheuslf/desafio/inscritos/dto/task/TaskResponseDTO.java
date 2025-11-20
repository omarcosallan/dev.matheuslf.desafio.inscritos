package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.dto.project.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        Status status,
        Priority priority,
        LocalDate dueDate,
        ProjectResponseDTO project
) {
}
