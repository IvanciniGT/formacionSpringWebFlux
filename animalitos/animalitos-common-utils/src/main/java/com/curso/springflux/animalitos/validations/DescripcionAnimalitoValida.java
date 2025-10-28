package com.curso.animalitos.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validación custom para descripción de animalito.
 * Reglas: Longitud máxima 500 caracteres
 */
@Documented
@Constraint(validatedBy = DescripcionAnimalitoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DescripcionAnimalitoValida {
    
    String message() default "La descripción del animalito no puede exceder los 500 caracteres";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}