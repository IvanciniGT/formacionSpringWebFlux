package com.curso.animalitos.rest.impl;

import com.curso.animalitos.rest.AnimalitosRestV1Controller;
import com.curso.animalitos.rest.dto.AnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosModificarAnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosNuevoAnimalitoRestV1DTO;
import com.curso.animalitos.service.api.AnimalitoService;
import com.curso.animalitos.service.api.exception.ErrorRepositorioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementación del controlador REST v1 para animalitos
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AnimalitoRestV1ControllerImpl implements AnimalitosRestV1Controller {

    private final AnimalitoService animalitoService;
    private final AnimalitoRestServiceMapper mapper;

    @Override
    public Mono<ResponseEntity<Flux<AnimalitoRestV1DTO>>> listarAnimalitos() {
        log.debug("Obteniendo todos los animalitos");
        
        var animalitosService = animalitoService.obtenerTodos();
        var animalitosRest = animalitosService.map( mapper::toRestDTO );
        // Aquí no tenemos size()... en este momento.. hasta que no acabe el flux, no habría ningún size.
        // log.debug("Encontrados {} animalitos", animalitosRest.size());
        // Eso podríamos programarlo para su ejecución una vez el flux se complete:
        animalitosRest.count().doOnNext( numero -> log.debug("Encontrados {} animalitos", numero) ).subscribe();
        return Mono.just(ResponseEntity.ok(animalitosRest));
    }

    @GetMapping("/{publicId}")
    @Override
    public Mono<ResponseEntity<AnimalitoRestV1DTO>> obtenerAnimalitoPorPublicId(@PathVariable String publicId) {
        log.debug("Obteniendo animalito con publicId: {}", publicId);
        
        var monoAnimalitoOptional = animalitoService.obtenerPorPublicId(publicId);

        return monoAnimalitoOptional.map( animalitoOptional -> {
                if (animalitoOptional.isEmpty()) {
                    log.debug("Animalito con publicId {} no encontrado", publicId);
                    return ResponseEntity.notFound().build();
                }

                var animalitoRest = mapper.toRestDTO(animalitoOptional.get());
                log.debug("Animalito encontrado: {}", animalitoRest.getNombre());
                return ResponseEntity.ok(animalitoRest);
        });
    }

    @Override
    public Mono<ResponseEntity<AnimalitoRestV1DTO>> crearAnimalito(DatosNuevoAnimalitoRestV1DTO datosNuevoAnimalito) {
        log.debug("Creando nuevo animalito: {}", datosNuevoAnimalito.getNombre());
        
        var datosNuevoService = mapper.toServiceDTO(datosNuevoAnimalito);
        var animalitoCreado = animalitoService.crear(datosNuevoService);

        return animalitoCreado.map( animalito -> {
            log.debug("Animalito creado con publicId: {}", animalito.getPublicId());
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toRestDTO(animalito));
        }).onErrorResume( error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()) );

    }

    @PutMapping("/{publicId}")
    @Override
    public Mono<ResponseEntity<AnimalitoRestV1DTO>> modificarAnimalito(@PathVariable String publicId, DatosModificarAnimalitoRestV1DTO datosModificarAnimalito) {
        log.debug("Modificando animalito con publicId: {}", publicId);
        
        var datosModificarService = mapper.toServiceDTO(datosModificarAnimalito);
        var animalitoModificado = animalitoService.modificar(publicId, datosModificarService);

        return animalitoModificado.map( animalitoOptional -> {
            if (animalitoOptional.isEmpty()) {
                log.debug("Animalito con publicId {} no encontrado para modificar", publicId);
                return ResponseEntity.notFound().build();
            }
            var animalitoRest = mapper.toRestDTO(animalitoOptional.get());
            log.debug("Animalito modificado: {}", animalitoRest.getNombre());
            return ResponseEntity.ok(animalitoRest);
        });
    }

    @DeleteMapping("/{publicId}")
    @Override
    public Mono<ResponseEntity<AnimalitoRestV1DTO>> borrarAnimalito(@PathVariable String publicId) {
        log.debug("Eliminando animalito con publicId: {}", publicId);
        
        var monoOptionalAnimalito = animalitoService.eliminar(publicId);

        return monoOptionalAnimalito.map( animalitoOptional -> {
            if (animalitoOptional.isEmpty()) {
                log.debug("Animalito con publicId {} no encontrado para eliminar", publicId);
                return ResponseEntity.notFound().build();
            }
            var animalitoRest = mapper.toRestDTO(animalitoOptional.get());
            log.debug("Animalito eliminado: {}", animalitoRest.getNombre());
            return ResponseEntity.ok(animalitoRest);
        });
    }
}