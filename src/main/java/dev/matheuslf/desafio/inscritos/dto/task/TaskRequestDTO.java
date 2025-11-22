package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank(message = "Title cannot be empty or null")
        @Size(min = 3, max = 150, message = "Title cannot be shorter than 3 characters or longer than 150 characters")
        String title,
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,
        @ValidStatus
        String status,
        @ValidPriority
        String priority,
        @FutureOrPresent(message = "Due date must be in the present or in the future")
        LocalDate dueDate,
        @NotBlank(message = "Project name cannot be empty or null")
        @Size(min = 3, max = 100, message = "Project name cannot be shorter than 3 characters or longer than 100 characters")
        String projectName,
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String assigneeEmail
) {
}
