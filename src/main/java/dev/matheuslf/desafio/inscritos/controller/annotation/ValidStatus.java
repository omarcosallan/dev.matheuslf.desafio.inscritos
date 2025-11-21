package dev.matheuslf.desafio.inscritos.controller.annotation;

import dev.matheuslf.desafio.inscritos.validator.StatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "Status inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
