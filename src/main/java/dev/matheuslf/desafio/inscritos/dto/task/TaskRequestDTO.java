package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank
        @Size(min = 5, max = 150)
        String title,
        @Size(max = 255)
        String description,
        @ValidStatus
        String status,
        @ValidPriority
        String priority,
        @FutureOrPresent
        LocalDate dueDate,
        @NotBlank
        @Size(min = 3, max = 100)
        String projectName,
        @NotBlank
        @Email
        @Size(max = 150)
        String assigneeEmail
) {
}
