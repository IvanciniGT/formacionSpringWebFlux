package com.curso.animalitos.service.api;

import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Servicio para gestión de animalitos
 */
public interface AnimalitoService {

    /**
     * Obtiene todos los animalitos
     * @return Lista de animalitos
     */
    Flux<AnimalitoServiceV1DTO> obtenerTodos();

    /**
     * Obtiene un animalito por su ID público
     * @param publicId ID público del animalito
     * @return Optional con el animalito si existe, vacío si no existe
     */
    Mono<Optional<AnimalitoServiceV1DTO>> obtenerPorPublicId(String publicId);

    /**
     * Crea un nuevo animalito
     * @param datosNuevo Datos del nuevo animalito
     * @return El animalito creado con su ID asignado
     */
    Mono<AnimalitoServiceV1DTO> crear(DatosNuevoAnimalitoServiceV1DTO datosNuevo);

    /**
     * Modifica un animalito existente
     * @param publicId ID público del animalito a modificar
     * @param datosModificar Nuevos datos del animalito
     * @return El animalito modificado
     */
    Mono<Optional<AnimalitoServiceV1DTO>> modificar(String publicId, DatosModificarAnimalitoServiceV1DTO datosModificar);

    /**
     * Elimina un animalito
     * @param publicId ID público del animalito a eliminar
     */
    Mono<Optional<AnimalitoServiceV1DTO>> eliminar(String publicId);
}