package com.curso.animalitos.repository.jpa;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.AnimalitosRepositoryException;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación JPA del repositorio de animalitos.
 * Utiliza Spring Data JPA para las operaciones de persistencia.
 * 
 * @Validated habilita la validación automática de parámetros con @Valid
 */
@Repository
@Validated
public class AnimalitoRepositoryJpa implements AnimalitoRepository {

    private final AnimalitoR2DBCRepository r2dbcRepository;

    public AnimalitoRepositoryJpa(AnimalitoR2DBCRepository jpaRepository) {
        this.r2dbcRepository = jpaRepository;
    }

    @Override
    public Flux<Animalito> findAll()  {
            return r2dbcRepository.findAll()
                    .map(entity -> (Animalito) entity);
    }

    @Override
    public Mono<Optional<Animalito>> findByPublicId(String publicId)  {
        try {
            return r2dbcRepository.findByPublicId(publicId)
                    .map(entity -> (Animalito) entity);
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al buscar animalito por publicId: " + publicId, e);
        }
    }

    @Override
    public Mono<Animalito> save(@Valid DatosNuevoAnimalito datosNuevoAnimalito) throws ConstraintViolationException {
        try {
            AnimalitoEntity entity = AnimalitoEntity.builder()
                    .nombre(datosNuevoAnimalito.nombre())
                    .especie(datosNuevoAnimalito.especie())
                    .edad(datosNuevoAnimalito.edad())
                    .descripcion(datosNuevoAnimalito.descripcion()!=null ? datosNuevoAnimalito.descripcion() : "")
                    .publicId(UUID.randomUUID().toString())
                    .build();



            return r2dbcRepository.save(entity);
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al guardar nuevo animalito", e);
        }
    }

    @Override
    public Mono<Optional<Animalito>> update(String publicId, @Valid DatosModificarAnimalito datosModificarAnimalito) throws ConstraintViolationException {
        try {
            Optional<AnimalitoEntity> entityExistente = r2dbcRepository.findByPublicId(publicId);
            
            if (entityExistente.isEmpty()) {
                throw new AnimalitosRepositoryException("No se encontró animalito con publicId: " + publicId);
            }
            
            AnimalitoEntity entity = entityExistente.get();
            entity.setNombre(datosModificarAnimalito.getNombre());
            entity.setEdad(datosModificarAnimalito.getEdad());
            entity.setDescripcion(datosModificarAnimalito.getDescripcion());
            
            return r2dbcRepository.save(entity);
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al actualizar animalito con publicId: " + publicId, e);
        }
    }

    @Override
    public Mono<Optional<Animalito>> deleteByPublicId(String publicId)  {
        try {
            Optional<AnimalitoEntity> entityExistente = r2dbcRepository.findByPublicId(publicId);
            
            if (entityExistente.isPresent()) {
                AnimalitoEntity entity = entityExistente.get();
                r2dbcRepository.delete(entity);
                return Optional.of(entity);
            }
            
            return Optional.empty();
        } catch (Exception e) {
            throw new AnimalitosRepositoryException("Error al eliminar animalito con publicId: " + publicId, e);
        }
    }
}