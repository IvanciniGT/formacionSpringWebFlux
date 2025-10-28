package com.curso.animalitos.repository;

/**
 * Interfaz que representa un animalito en el sistema.
 * Define el contrato para acceder a los datos de un animalito.
 */
public interface Animalito {
    
    /**
     * Identificador público del animalito (UUID)
     * @return publicId único del animalito
     */
    String getPublicId();
    
    /**
     * Nombre del animalito
     * @return nombre del animalito
     */
    String getNombre();
    
    /**
     * Especie del animalito
     * @return especie del animalito
     */
    String getEspecie();
    
    /**
     * Edad del animalito en años
     * @return edad del animalito
     */
    Integer getEdad();
    
    /**
     * Descripción del animalito
     * @return descripción del animalito
     */
    String getDescripcion();
}