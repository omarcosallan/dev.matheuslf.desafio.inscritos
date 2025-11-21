package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.controller.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.controller.annotation.ValidStatus;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTaskDTO(
        @Size(min = 5, max = 150)
        String title,
        @Size(max = 255)
        String description,
        @ValidStatus
        String status,
        @ValidPriority
        String priority,
        @FutureOrPresent
        LocalDate dueDate
) {
}
