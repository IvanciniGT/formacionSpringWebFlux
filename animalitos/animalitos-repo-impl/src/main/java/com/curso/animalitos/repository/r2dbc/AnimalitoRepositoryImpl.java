package com.curso.animalitos.repository.r2dbc;

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
                    .map(Animalito.class::cast)
                    .onErrorMap( e -> new AnimalitosRepositoryException("Error al obtener todos los animalitos", e) );
    }

    @Override
    public Mono<Optional<Animalito>> findByPublicId(String publicId)  {
            return r2dbcRepository.findByPublicId(publicId)
                    .map( optionalAnimalito -> optionalAnimalito.map( Animalito.class::cast ) )
                    .onErrorMap( e -> new AnimalitosRepositoryException("Error al obtener animalito con publicId: " + publicId, e) );
    }

    @Override
    public Mono<Animalito> save(@Valid DatosNuevoAnimalito datosNuevoAnimalito) throws ConstraintViolationException {
            AnimalitoEntity entity = AnimalitoEntity.builder()
                    .nombre(datosNuevoAnimalito.nombre())
                    .especie(datosNuevoAnimalito.especie())
                    .edad(datosNuevoAnimalito.edad())
                    .descripcion(datosNuevoAnimalito.descripcion()!=null ? datosNuevoAnimalito.descripcion() : "")
                    .publicId(UUID.randomUUID().toString())
                    .build();
            return r2dbcRepository.save(entity)
                    .map(Animalito.class::cast)
                    .onErrorMap( e -> new AnimalitosRepositoryException("Error al guardar nuevo animalito", e) );
    }

    @Override
    public Mono<Optional<Animalito>> update(String publicId, @Valid DatosModificarAnimalito datosModificarAnimalito) throws ConstraintViolationException {
        return r2dbcRepository.findByPublicId(publicId)
                .map(
                        entidadOpcional -> entidadOpcional.map(animalitoEntidad -> {
                                animalitoEntidad.setNombre(datosModificarAnimalito.getNombre());
                                animalitoEntidad.setEdad(datosModificarAnimalito.getEdad());
                                animalitoEntidad.setDescripcion(datosModificarAnimalito.getDescripcion()!=null ? datosModificarAnimalito.getDescripcion() : "");
                                return animalitoEntidad;
                        })
                ) // Mono<Optional<AnimalitoEntity>>
/*                .flatMap( entidadOpcional -> {
                         if(entidadOpcional.isPresent()){
                                return r2dbcRepository.save(entidadOpcional.get())
                                        .map(Optional::of);
                            }
                         return Mono.just(Optional.empty());
                    }
                );*/

                .flatMap( entidadOpcional ->
                            entidadOpcional.
                                    <Mono< Optional<Animalito>>>map(animalitoEntity -> r2dbcRepository.save(animalitoEntity)
                                    .map(Optional::of)).orElseGet(() -> Mono.just(Optional.empty()))
                )
                .onErrorMap( e -> new AnimalitosRepositoryException("Error al guardar nuevo animalito", e) );

        // FlatMap nos permite aplicar transformaciones que devuelven Mono/Flux, desde un Mono/Flux inicial.
        // Encadenar varias operaciones asíncronas.
        // El map se ejecuta sincronicamente

    }

    @Override
    public Mono<Optional<Animalito>> deleteByPublicId(String publicId)  {
        Mono<Optional<AnimalitoEntity>> entityExistente = r2dbcRepository.findByPublicId(publicId);
        return entityExistente.flatMap( entidadOpcional -> {
            if (entidadOpcional.isPresent()) {
                return r2dbcRepository.delete(entidadOpcional.get())
                        .thenReturn(entidadOpcional.map(Animalito.class::cast));
            }else {
                return Mono.just(Optional.empty());
            }
        });
    }
}