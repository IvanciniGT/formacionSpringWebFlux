package com.curso.animalitos.repository;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interfaz del repositorio para gestionar animalitos.
 * Define las operaciones CRUD básicas para animalitos.
 * Todos los métodos pueden lanzar AnimalitosRepositoryException.
 */
public interface AnimalitoRepository {
    
    /**
     * Obtiene todos los animalitos del repositorio
     * @return Lista de todos los animalitos
     */
    Flux<Animalito> findAll();
    
    /**
     * Busca un animalito por su identificador público
     * @param publicId identificador público del animalito
     * @return Optional con el animalito si existe, vacío si no existe
     */
    Mono<Optional<Animalito>> findByPublicId(String publicId) ;
    
    /**
     * Guarda un nuevo animalito en el repositorio
     * @param datosNuevoAnimalito datos del animalito a crear (validados)
     * @return El animalito creado con su identificador público asignado
     * @throws ConstraintViolationException si los datos son inválidos
     */
    Mono<Animalito> save(@Valid DatosNuevoAnimalito datosNuevoAnimalito) throws ConstraintViolationException;
    
    /**
     * Actualiza un animalito existente en el repositorio
     * @param publicId identificador público del animalito a actualizar
     * @param datosModificarAnimalito nuevos datos del animalito (validados)
     * @return El animalito actualizado
     * @throws ConstraintViolationException si los datos son inválidos
     */
    Mono<Optional<Animalito>> update(String publicId, @Valid DatosModificarAnimalito datosModificarAnimalito) throws ConstraintViolationException;
    
    /**
     * Elimina un animalito del repositorio por su identificador público
     * @param publicId identificador público del animalito a eliminar
     * @return Optional con el animalito eliminado si existía, vacío si no existía
     */
    Mono<Optional<Animalito>> deleteByPublicId(String publicId);
}