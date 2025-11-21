package dev.matheuslf.desafio.inscritos.controller.annotation;

import dev.matheuslf.desafio.inscritos.utils.PriorityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriorityValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriority {
    String message() default "Prioridade inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
