package com.curso.animalitos.rest.dto;

import com.curso.animalitos.validations.DescripcionAnimalitoValida;
import com.curso.animalitos.validations.EdadAnimalitoValida;
import com.curso.animalitos.validations.EspecieAnimalitoValida;
import com.curso.animalitos.validations.NombreAnimalitoValido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para modificar un animalito existente a través de la API REST v1.
 * Contiene los campos que pueden ser modificados (no incluye publicId).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "DatosModificarAnimalitoV1",
    description = "Datos que pueden ser modificados de un animalito existente"
)
public class DatosModificarAnimalitoRestV1DTO {

    @NombreAnimalitoValido
    @Schema(
        description = "Nuevo nombre del animalito. Debe comenzar con mayúscula",
        example = "Firulais Actualizado",
        minLength = 2,
        maxLength = 50,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String nombre;

    @EspecieAnimalitoValida
    @Schema(
        description = "Nueva especie del animalito",
        example = "Perro",
        minLength = 2,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String especie;

    @EdadAnimalitoValida
    @Schema(
        description = "Nueva edad del animalito en años",
        example = "4",
        minimum = "0",
        maximum = "1000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Integer edad;

    @DescripcionAnimalitoValida
    @Schema(
        description = "Nueva descripción del animalito",
        example = "Un perro muy juguetón, cariñoso y bien entrenado",
        maxLength = 500,
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    String descripcion;
}