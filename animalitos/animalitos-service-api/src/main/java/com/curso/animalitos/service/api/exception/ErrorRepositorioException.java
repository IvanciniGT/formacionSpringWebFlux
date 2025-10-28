package com.curso.animalitos.service.api.exception;

/**
 * Excepción lanzada cuando hay errores de validación en los datos
 */
public class ErrorRepositorioException extends RuntimeException {

    public ErrorRepositorioException(String mensaje) {
        super(mensaje);
    }

    public ErrorRepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}