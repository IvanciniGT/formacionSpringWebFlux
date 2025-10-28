package com.curso.animalitos.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator para descripción de animalito.
 * Implementa la lógica de validación para @DescripcionAnimalitoValida
 */
public class DescripcionAnimalitoValidator implements ConstraintValidator<DescripcionAnimalitoValida, String> {

    @Override
    public void initialize(DescripcionAnimalitoValida constraintAnnotation) {
        // No hay configuración específica necesaria
    }

    @Override
    public boolean isValid(String descripcion, ConstraintValidatorContext context) {
        // Null se considera válido (se puede manejar con valor por defecto)
        if (descripcion == null) {
            return true;
        }
        
        // Validar longitud máxima (500 caracteres)
        return descripcion.length() <= 500;
    }
}