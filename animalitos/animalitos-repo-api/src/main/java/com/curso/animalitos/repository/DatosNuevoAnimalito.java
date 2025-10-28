package com.curso.animalitos.repository;

import com.curso.animalitos.validations.DescripcionAnimalitoValida;
import com.curso.animalitos.validations.EdadAnimalitoValida;
import com.curso.animalitos.validations.EspecieAnimalitoValida;
import com.curso.animalitos.validations.NombreAnimalitoValido;

/**
 * Record que representa los datos necesarios para crear un nuevo animalito.
 * Incluye todas las validaciones correspondientes.
 * 
 * Ejemplo de inmutabilidad con record nativo de Java (desde Java 14)
 * Los records son automáticamente inmutables, finales y con constructor, getters, equals, hashCode y toString
 */
public record DatosNuevoAnimalito( // Java 17
// En realidad, pasamos de ellos. Es más sencillo usar lombok @Value + @Builder
// Y da más funciones, como el patrón builder
    
    @NombreAnimalitoValido
    String nombre,
    
    @EspecieAnimalitoValida
    String especie,
    
    @EdadAnimalitoValida
    Integer edad,
    
    @DescripcionAnimalitoValida
    String descripcion
) {
    
    /**
     * Constructor compacto para aplicar valores por defecto
     */
    public DatosNuevoAnimalito {
        // Si descripcion es null, asignar valor por defecto
        if (descripcion == null) {
            descripcion = "";
        }
    }
    
    /**
     * Constructor de conveniencia sin descripción (usa valor por defecto)
     */
    public DatosNuevoAnimalito(String nombre, String especie, Integer edad) {
        this(nombre, especie, edad, "");
    }
}