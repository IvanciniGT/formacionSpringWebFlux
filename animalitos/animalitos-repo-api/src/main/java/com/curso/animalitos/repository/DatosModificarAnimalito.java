package com.curso.animalitos.repository;

import com.curso.animalitos.validations.DescripcionAnimalitoValida;
import com.curso.animalitos.validations.EdadAnimalitoValida;
import com.curso.animalitos.validations.NombreAnimalitoValido;
import lombok.Builder;
import lombok.Value;

/**
 * Clase que representa los datos necesarios para modificar un animalito existente.
 * Incluye todas las validaciones correspondientes.
 * Nota: La especie no se puede modificar una vez creado el animalito.
 * 
 * Ejemplo de inmutabilidad con Lombok @Value + @Builder
 */
@Value // Inmutable class
@Builder
public class DatosModificarAnimalito {
    
    @NombreAnimalitoValido
    String nombre;
    
    @EdadAnimalitoValida
    Integer edad;
    
    @DescripcionAnimalitoValida
    @Builder.Default
    String descripcion = "";
}