package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.annotation.ValidPriority;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PriorityValidator implements ConstraintValidator<ValidPriority, String> {
    private static final Set<String> VALID_PRIORITIES = Arrays.stream(Priority.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String priority, ConstraintValidatorContext constraintValidatorContext) {
        if (priority == null) return true;
        return VALID_PRIORITIES.contains(priority.toUpperCase());
    }
}
