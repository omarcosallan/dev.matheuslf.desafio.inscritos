package dev.matheuslf.desafio.inscritos.dto.project;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectUpdateDTO(
        @Size(min = 3, max = 100)
        String name,
        @Size(max = 255)
        String description,
        @FutureOrPresent
        LocalDate startDate,
        @Future
        LocalDate endDate,
        @Email
        @Size(max = 150)
        String ownerEmail
) {
}
