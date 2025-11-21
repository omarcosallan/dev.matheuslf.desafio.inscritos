package dev.matheuslf.desafio.inscritos.validator;

import dev.matheuslf.desafio.inscritos.annotation.ValidRole;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    private static final Set<String> VALID_ROLES = Arrays.stream(Role.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        if (role == null) return true;
        return VALID_ROLES.contains(role.toUpperCase());
    }
}
