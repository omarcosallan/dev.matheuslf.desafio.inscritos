package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.annotation.ValidStatus;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    private static final Set<String> VALID_PRIORITIES = Arrays.stream(Status.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        if (status == null) return true;
        return VALID_PRIORITIES.contains(status.toUpperCase());
    }
}
