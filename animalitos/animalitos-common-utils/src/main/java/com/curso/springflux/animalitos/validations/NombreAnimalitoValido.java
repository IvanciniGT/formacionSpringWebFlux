package com.curso.animalitos.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

/**
 * Validación custom para nombres de animalito.
 * Reglas: No nulo, no vacío, longitud 2-50, Primera letra mayúscula
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = NombreAnimalitoValido.MIN_LENGTH, max = NombreAnimalitoValido.MAX_LENGTH, message = "El nombre del animalito debe tener entre 2 y 50 caracteres")
@Pattern(regexp = NombreAnimalitoValido.CAPITALIZED_PATTERN, message = "El nombre del animalito debe comenzar con mayúscula")
@NotNull(message = "El nombre del animalito no puede ser nulo")
public @interface NombreAnimalitoValido {
    
    int MIN_LENGTH = 2;
    int MAX_LENGTH = 50;
    String CAPITALIZED_PATTERN = "^[A-Z].*$";
    
    String message() default "El nombre del animalito no es válido";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}