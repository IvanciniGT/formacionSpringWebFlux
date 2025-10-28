package com.curso.animalitos.service.api.exception;

/**
 * Excepción lanzada cuando no se encuentra un animalito
 */
public class AnimalitoNoEncontradoException extends RuntimeException {
    
    public AnimalitoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public AnimalitoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}