package com.curso.animalitos.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

/**
 * DTO que representa un animalito en la API REST v1.
 * Contiene todos los datos del animalito incluyendo su identificador público único.
 */
@Value
@Builder
@Schema(
    name = "AnimalitoV1",
    description = "Representación completa de un animalito en la API REST v1"
)
public class AnimalitoRestV1DTO {

    @Schema(
        description = "Identificador público único del animalito",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String publicId;

    @Schema(
        description = "Nombre del animalito",
        example = "Firulais",
        minLength = 2,
        maxLength = 50,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String nombre;

    @Schema(
        description = "Especie del animalito",
        example = "Perro",
        minLength = 3,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String especie;

    @Schema(
        description = "Edad del animalito en años",
        example = "3",
        minimum = "0",
        maximum = "1000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Integer edad;

    @Schema(
        description = "Descripción opcional del animalito",
        example = "Un perro muy juguetón y cariñoso",
        maxLength = 500,
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    String descripcion;
}