package com.curso.animalitos.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuestas de error estándar de la API REST v1.
 */
@Schema(
    name = "ErrorResponse",
    description = "Respuesta estándar para errores de la API"
)
public record ErrorResponseRestV1DTO(
    @Schema(description = "Código de estado HTTP", example = "400")
    int status,
    
    @Schema(description = "Mensaje de error", example = "Datos de entrada inválidos")
    String message,
    
    @Schema(description = "Timestamp del error", example = "2023-10-22T15:30:45.123Z")
    String timestamp,
    
    @Schema(description = "Ruta del endpoint que generó el error", example = "/api/v1/animalitos/123")
    String path
) {}