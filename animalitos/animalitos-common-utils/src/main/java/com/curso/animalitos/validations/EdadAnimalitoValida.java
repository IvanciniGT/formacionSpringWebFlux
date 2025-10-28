package com.curso.animalitos.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/**
 * Validación custom para edad de animalito.
 * Reglas: No nulo, valor mínimo 0, valor máximo 1000
 */
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Min(value = EdadAnimalitoValida.MIN_AGE, message = "La edad del animalito no puede ser menor que 0")
@Max(value = EdadAnimalitoValida.MAX_AGE, message = "La edad del animalito no puede ser mayor que 1000")
@NotNull(message = "La edad del animalito no puede ser nula")
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadAnimalitoValida {

    int MIN_AGE = 0;
    int MAX_AGE = 1000;
    
    String message() default "La edad del animalito debe estar entre 0 y 1000 años";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}