package com.curso.animalitos.service.api.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos
 */
public class ErrorValidacionException extends RuntimeException {
    
    public ErrorValidacionException(String mensaje) {
        super(mensaje);
    }
    
    public ErrorValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}