package com.curso.animalitos.repository;

import jakarta.validation.Valid;
import java.util.List;
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
     * @throws AnimalitosRepositoryException si hay error al acceder al repositorio
     */
    List<Animalito> findAll() throws AnimalitosRepositoryException;
    
    /**
     * Busca un animalito por su identificador público
     * @param publicId identificador público del animalito
     * @return Optional con el animalito si existe, vacío si no existe
     * @throws AnimalitosRepositoryException si hay error al acceder al repositorio
     */
    Optional<Animalito> findByPublicId(String publicId) throws AnimalitosRepositoryException;
    
    /**
     * Guarda un nuevo animalito en el repositorio
     * @param datosNuevoAnimalito datos del animalito a crear (validados)
     * @return El animalito creado con su identificador público asignado
     * @throws AnimalitosRepositoryException si hay error al guardar o si los datos son inválidos
     */
    Animalito save(@Valid DatosNuevoAnimalito datosNuevoAnimalito) throws AnimalitosRepositoryException;
    
    /**
     * Actualiza un animalito existente en el repositorio
     * @param publicId identificador público del animalito a actualizar
     * @param datosModificarAnimalito nuevos datos del animalito (validados)
     * @return El animalito actualizado
     * @throws AnimalitosRepositoryException si hay error al actualizar, si el animalito no existe, o si los datos son inválidos
     */
    Animalito update(String publicId, @Valid DatosModificarAnimalito datosModificarAnimalito) throws AnimalitosRepositoryException;
    
    /**
     * Elimina un animalito del repositorio por su identificador público
     * @param publicId identificador público del animalito a eliminar
     * @return Optional con el animalito eliminado si existía, vacío si no existía
     * @throws AnimalitosRepositoryException si hay error al eliminar
     */
    Optional<Animalito> deleteByPublicId(String publicId) throws AnimalitosRepositoryException;
}