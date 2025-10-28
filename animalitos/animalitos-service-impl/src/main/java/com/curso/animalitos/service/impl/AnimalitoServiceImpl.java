package com.curso.animalitos.service.impl;

import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import com.curso.animalitos.service.api.AnimalitoService;
import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.exception.ErrorRepositorioException;
import com.curso.animalitos.service.api.exception.ErrorValidacionException;
import com.curso.animalitos.service.impl.mapper.AnimalitoServiceMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalitoServiceImpl implements AnimalitoService {

    private final AnimalitoRepository animalitoRepository;
    private final AnimalitoServiceMapper mapper;

    public AnimalitoServiceImpl(AnimalitoRepository animalitoRepository, AnimalitoServiceMapper mapper) {
        this.animalitoRepository = animalitoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<AnimalitoServiceV1DTO> crear(DatosNuevoAnimalitoServiceV1DTO datosNuevo) {
        // El problema es que la excepción viene del repositorio al hacer el save...
        // Bueno... relamnete no al hacer el save, cuando se haga la query a la BBDD.
        // No es cuando me devuelven el mono, sino cuando el valor de dentro del mono se calcula.
        // Realmente, hay 2 causas para poder recibir una excepción:
        // - BBDD No disponible, o problema de otro tipo en BBDD
        //   Estas otras no las puedo procesar aquí, porque son asíncronas... puedo dejar programado que
        //   cuando se produzca una excption de cualquier tipo nosotros la trasnformemos en otra: ErrorRepositorioException
        // - Error al validar un dato (síncronas). Las sincronas, las podemos capturar aquí, con el try-catch.
        //   y devolver un mono con error de validación.
        try {
            DatosNuevoAnimalito datosRepo = mapper.toRepoDatosNuevo(datosNuevo);
            return animalitoRepository.save(datosRepo)
                                      .map(mapper::toServiceDTO)
                                      //cuando se produzca una excption de cualquier tipo nosotros la trasnformemos en otra: ErrorRepositorioException
                                      .onErrorMap(e -> new ErrorRepositorioException("Error al crear animalito: " + e.getMessage(), e));

        } catch (ConstraintViolationException e) { // Esta se produce de forma síncrona
            return Mono.error(new ErrorValidacionException("Error de validación al crear animalito: " + e.getMessage(), e));
        }
    }

    @Override
    public Flux<AnimalitoServiceV1DTO> obtenerTodos() {
            return animalitoRepository.findAll()
                    .map(mapper::toServiceDTO)
                    .onErrorMap(e -> new ErrorRepositorioException("Error al obtener todos los animalitos: " + e.getMessage(), e));
    }

    @Override
    public Mono<Optional<AnimalitoServiceV1DTO>> obtenerPorPublicId(String publicId) {
            return animalitoRepository.findByPublicId(publicId)
                    .map( animalitoOpcional -> animalitoOpcional.map(mapper::toServiceDTO))
                    .onErrorMap(e -> new ErrorRepositorioException("Error al obtener animalito por publicId: " + e.getMessage(), e));
    }

    @Override
    public Mono<Optional<AnimalitoServiceV1DTO>> modificar(String publicId, DatosModificarAnimalitoServiceV1DTO datosModificar) {


        try {
            DatosModificarAnimalito datosRepo = mapper.toRepoDatosModificar(datosModificar);
            return animalitoRepository.update(publicId, datosRepo)
                    .map(animalitoOpcional -> animalitoOpcional.map(mapper::toServiceDTO))
                    //cuando se produzca una excption de cualquier tipo nosotros la trasnformemos en otra: ErrorRepositorioException
                    .onErrorMap(e -> new ErrorRepositorioException("Error al modificar animalito: " + e.getMessage(), e));

        } catch (ConstraintViolationException e) { // Esta se produce de forma síncrona
            return Mono.error(new ErrorValidacionException("Error de validación al modificar animalito: " + e.getMessage(), e));
        }

    }

    @Override
    public Mono<Optional<AnimalitoServiceV1DTO>> eliminar(String publicId) {
            var eliminated = animalitoRepository.deleteByPublicId(publicId);
            return eliminated
                    .map(animalitoOpcional -> animalitoOpcional.map(mapper::toServiceDTO))
                    .onErrorMap(e -> new ErrorRepositorioException("Error al eliminar animalito: " + e.getMessage(), e));
    }
}