package com.curso.animalitos.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para AnimalitoEntity.
 * Extiende JpaRepository para operaciones CRUD básicas.
 */
@Repository
public interface AnimalitoJpaRepository extends JpaRepository<AnimalitoEntity, Long> {
    
    /**
     * Busca un animalito por su identificador público (UUID)
     * @param publicId identificador público del animalito
     * @return Optional con la entidad si existe
     */
    Optional<AnimalitoEntity> findByPublicId(String publicId);
    
    /**
     * Verifica si existe un animalito con el publicId dado
     * @param publicId identificador público del animalito
     * @return true si existe, false si no
     */
    boolean existsByPublicId(String publicId);
}