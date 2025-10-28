package com.curso.animalitos.rest;

import com.curso.animalitos.rest.dto.AnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosModificarAnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosNuevoAnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.ErrorResponseRestV1DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interfaz del controlador REST para la gestión de animalitos - API v1.
 * Define todos los endpoints disponibles con documentación OpenAPI completa.
 */
@Tag(
    name = "Animalitos API v1",
    description = "API REST para la gestión de animalitos - Versión 1"
)
@RequestMapping("/api/v1/animalitos")
public interface AnimalitosRestV1Controller {

    @Operation(
        summary = "Listar todos los animalitos",
        description = "Obtiene la lista completa de todos los animalitos registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de animalitos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnimalitoRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<AnimalitoRestV1DTO>> listarAnimalitos();

    @Operation(
        summary = "Obtener un animalito por su ID público",
        description = "Busca y retorna un animalito específico utilizando su identificador público único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Animalito encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnimalitoRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Animalito no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID público inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        )
    })
    @GetMapping("/{publicId}")
    ResponseEntity<AnimalitoRestV1DTO> obtenerAnimalitoPorPublicId(
        @Parameter(
            description = "Identificador público único del animalito",
            example = "550e8400-e29b-41d4-a716-446655440000",
            required = true
        )
        @PathVariable String publicId
    );

    @Operation(
        summary = "Crear un nuevo animalito",
        description = "Registra un nuevo animalito en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Animalito creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnimalitoRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        )
    })
    @PostMapping
    ResponseEntity<AnimalitoRestV1DTO> crearAnimalito(
        @Parameter(
            description = "Datos del nuevo animalito a crear",
            required = true
        )
        @Valid @RequestBody DatosNuevoAnimalitoRestV1DTO datosNuevoAnimalito
    );

    @Operation(
        summary = "Modificar un animalito existente",
        description = "Actualiza los datos de un animalito existente identificado por su ID público"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Animalito modificado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnimalitoRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Animalito no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o ID público inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        )
    })
    @PutMapping("/{publicId}")
    ResponseEntity<AnimalitoRestV1DTO> modificarAnimalito(
        @Parameter(
            description = "Identificador público único del animalito a modificar",
            example = "550e8400-e29b-41d4-a716-446655440000",
            required = true
        )
        @PathVariable String publicId,
        
        @Parameter(
            description = "Nuevos datos del animalito",
            required = true
        )
        @Valid @RequestBody DatosModificarAnimalitoRestV1DTO datosModificarAnimalito
    );

    @Operation(
        summary = "Eliminar un animalito",
        description = "Elimina un animalito del sistema utilizando su identificador público único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Animalito eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnimalitoRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Animalito no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID público inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseRestV1DTO.class)
            )
        )
    })
    @DeleteMapping("/{publicId}")
    ResponseEntity<AnimalitoRestV1DTO> borrarAnimalito(
        @Parameter(
            description = "Identificador público único del animalito a eliminar",
            example = "550e8400-e29b-41d4-a716-446655440000",
            required = true
        )
        @PathVariable String publicId
    );


}