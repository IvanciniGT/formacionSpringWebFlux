package com.curso.animalitos.repository;

/**
 * Excepción específica para errores del repositorio de animalitos.
 * Puede ser lanzada por cualquier método del repositorio.
 */
public class AnimalitosRepositoryException extends Exception {
    
    /**
     * Constructor con mensaje de error
     * @param message mensaje descriptivo del error
     */
    public AnimalitosRepositoryException(String message) {
        super(message);
    }
    
    /**
     * Constructor con mensaje de error y causa raíz
     * @param message mensaje descriptivo del error
     * @param cause causa raíz del error
     */
    public AnimalitosRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor con causa raíz
     * @param cause causa raíz del error
     */
    public AnimalitosRepositoryException(Throwable cause) {
        super(cause);
    }
}