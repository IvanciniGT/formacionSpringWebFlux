package com.curso.animalitos.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/**
 * Validación custom para especie de animalito.
 * Reglas: No nulo, no vacío, longitud 3-30
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = EspecieAnimalitoValida.MIN_LENGTH, max = EspecieAnimalitoValida.MAX_LENGTH, message = "La especie del animalito debe tener entre 3 y 30 caracteres")
@NotNull(message = "La especie del animalito no puede ser nula")
public @interface EspecieAnimalitoValida {
    
    int MIN_LENGTH = 3;
    int MAX_LENGTH = 30;

    String message() default "La especie del animalito no es válida";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}

/*
 * Qué otras anotaciones estanda de jakarta.validation teneis:
 * @NotNull
 * @NotEmpty
 * @NotBlank
 * @Size
 * @Min
 * @Max
 * @Positive
 * @PositiveOrZero
 * @Negative
 * @NegativeOrZero
 * @Email
 * @Pattern
 */