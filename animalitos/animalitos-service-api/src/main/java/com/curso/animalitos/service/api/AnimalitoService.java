package com.curso.animalitos.service.api;

import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.exception.AnimalitoNoEncontradoException;
import com.curso.animalitos.service.api.exception.ErrorValidacionException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de animalitos
 */
public interface AnimalitoService {

    /**
     * Obtiene todos los animalitos
     * @return Lista de animalitos
     */
    List<AnimalitoServiceV1DTO> obtenerTodos();

    /**
     * Obtiene un animalito por su ID público
     * @param publicId ID público del animalito
     * @return Optional con el animalito si existe, vacío si no existe
     */
    Optional<AnimalitoServiceV1DTO> obtenerPorPublicId(String publicId);

    /**
     * Crea un nuevo animalito
     * @param datosNuevo Datos del nuevo animalito
     * @return El animalito creado con su ID asignado
     * @throws ErrorValidacionException si los datos no son válidos
     */
    AnimalitoServiceV1DTO crear(DatosNuevoAnimalitoServiceV1DTO datosNuevo);

    /**
     * Modifica un animalito existente
     * @param publicId ID público del animalito a modificar
     * @param datosModificar Nuevos datos del animalito
     * @return El animalito modificado
     * @throws AnimalitoNoEncontradoException si el animalito no existe
     * @throws ErrorValidacionException si los datos no son válidos
     */
    AnimalitoServiceV1DTO modificar(String publicId, DatosModificarAnimalitoServiceV1DTO datosModificar);

    /**
     * Elimina un animalito
     * @param publicId ID público del animalito a eliminar
     * @throws AnimalitoNoEncontradoException si el animalito no existe
     */
    void eliminar(String publicId);
}