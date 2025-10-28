package com.curso.animalitos.repository.jpa;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Repositorio Spring Data R2DBC para AnimalitoEntity.
 * Extiende ReactiveCrudRepository para operaciones CRUD básicas.
 */
@Repository
public interface AnimalitoR2DBCRepository extends ReactiveCrudRepository<AnimalitoEntity, Long> {
    
    /**
     * Busca un animalito por su identificador público (UUID)
     * @param publicId identificador público del animalito
     * @return Optional con la entidad si existe
     */
    Mono<Optional<AnimalitoEntity>> findByPublicId(String publicId);
    
    /**
     * Verifica si existe un animalito con el publicId dado
     * @param publicId identificador público del animalito
     * @return true si existe, false si no
     */
    boolean existsByPublicId(String publicId);
}