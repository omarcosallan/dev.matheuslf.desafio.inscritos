package dev.matheuslf.desafio.inscritos.dto.task;

import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;

public record TaskStatusUpdateDTO(
        @ValidStatus
        String status
) {
}
