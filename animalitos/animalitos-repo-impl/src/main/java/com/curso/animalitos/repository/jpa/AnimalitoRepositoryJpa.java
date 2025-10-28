package com.curso.animalitos.repository.jpa;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.AnimalitosRepositoryException;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de animalitos.
 * Utiliza Spring Data JPA para las operaciones de persistencia.
 * 
 * @Validated habilita la validación automática de parámetros con @Valid
 */
@Repository
@Validated
public class AnimalitoRepositoryJpa implements AnimalitoRepository {

    private final AnimalitoJpaRepository jpaRepository;

    public AnimalitoRepositoryJpa(AnimalitoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Animalito> findAll() throws AnimalitosRepositoryException {
        try {
            return jpaRepository.findAll()
                    .stream()
                    .map(entity -> (Animalito) entity)
                    .toList();
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al obtener todos los animalitos", e);
        }
    }

    @Override
    public Optional<Animalito> findByPublicId(String publicId) throws AnimalitosRepositoryException {
        try {
            return jpaRepository.findByPublicId(publicId)
                    .map(entity -> (Animalito) entity);
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al buscar animalito por publicId: " + publicId, e);
        }
    }

    @Override
    public Animalito save(@Valid DatosNuevoAnimalito datosNuevoAnimalito) throws AnimalitosRepositoryException {
        try {
            AnimalitoEntity entity = AnimalitoEntity.builder()
                    .nombre(datosNuevoAnimalito.nombre())
                    .especie(datosNuevoAnimalito.especie())
                    .edad(datosNuevoAnimalito.edad())
                    .descripcion(datosNuevoAnimalito.descripcion())
                    .build();
            
            return jpaRepository.save(entity);
        } catch (ConstraintViolationException e) {
            // Re-lanzar excepciones de validación para que los tests las capturen
            throw e;
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al guardar nuevo animalito", e);
        }
    }

    @Override
    public Animalito update(String publicId, @Valid DatosModificarAnimalito datosModificarAnimalito) throws AnimalitosRepositoryException {
        try {
            Optional<AnimalitoEntity> entityExistente = jpaRepository.findByPublicId(publicId);
            
            if (entityExistente.isEmpty()) {
                throw new AnimalitosRepositoryException("No se encontró animalito con publicId: " + publicId);
            }
            
            AnimalitoEntity entity = entityExistente.get();
            entity.setNombre(datosModificarAnimalito.getNombre());
            entity.setEdad(datosModificarAnimalito.getEdad());
            entity.setDescripcion(datosModificarAnimalito.getDescripcion());
            
            return jpaRepository.save(entity);
        } catch (ConstraintViolationException | AnimalitosRepositoryException e) {
            // Re-lanzar excepciones de validación y de repositorio tal como están
            throw e;
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al actualizar animalito con publicId: " + publicId, e);
        }
    }

    @Override
    public Optional<Animalito> deleteByPublicId(String publicId) throws AnimalitosRepositoryException {
        try {
            Optional<AnimalitoEntity> entityExistente = jpaRepository.findByPublicId(publicId);
            
            if (entityExistente.isPresent()) {
                AnimalitoEntity entity = entityExistente.get();
                jpaRepository.delete(entity);
                return Optional.of(entity);
            }
            
            return Optional.empty();
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al eliminar animalito con publicId: " + publicId, e);
        }
    }
}