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
 * DTO para crear un nuevo animalito a través de la API REST v1.
 * Contiene todos los datos necesarios para crear un animalito excepto el publicId.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "DatosNuevoAnimalitoV1",
    description = "Datos necesarios para crear un nuevo animalito"
)
public class DatosNuevoAnimalitoRestV1DTO {

    @NombreAnimalitoValido
    @Schema(
        description = "Nombre del animalito. Debe comenzar con mayúscula",
        example = "Firulais",
        minLength = 2,
        maxLength = 50,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String nombre;

    @EspecieAnimalitoValida
    @Schema(
        description = "Especie del animalito",
        example = "Perro",
        minLength = 3,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String especie;

    @EdadAnimalitoValida
    @Schema(
        description = "Edad del animalito en años",
        example = "3",
        minimum = "0",
        maximum = "1000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Integer edad;

    @DescripcionAnimalitoValida
    @Schema(
        description = "Descripción opcional del animalito",
        example = "Un perro muy juguetón y cariñoso",
        maxLength = 500,
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = ""
    )
    String descripcion;
}