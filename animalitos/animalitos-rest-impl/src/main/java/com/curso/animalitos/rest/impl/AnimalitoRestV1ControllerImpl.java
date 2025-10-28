package com.curso.animalitos.rest.impl;

import com.curso.animalitos.rest.AnimalitosRestV1Controller;
import com.curso.animalitos.rest.dto.AnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosModificarAnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosNuevoAnimalitoRestV1DTO;
import com.curso.animalitos.service.api.AnimalitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<AnimalitoRestV1DTO>> listarAnimalitos() {
        log.debug("Obteniendo todos los animalitos");
        
        var animalitosService = animalitoService.obtenerTodos();
        var animalitosRest = mapper.toRestDTOList(animalitosService);
        
        log.debug("Encontrados {} animalitos", animalitosRest.size());
        return ResponseEntity.ok(animalitosRest);
    }

    @Override
    public ResponseEntity<AnimalitoRestV1DTO> obtenerAnimalitoPorPublicId(@PathVariable String publicId) {
        log.debug("Obteniendo animalito con publicId: {}", publicId);
        
        var animalitoOptional = animalitoService.obtenerPorPublicId(publicId);
        
        if (animalitoOptional.isEmpty()) {
            log.debug("Animalito con publicId {} no encontrado", publicId);
            return ResponseEntity.notFound().build();
        }
        
        var animalitoRest = mapper.toRestDTO(animalitoOptional.get());
        log.debug("Animalito encontrado: {}", animalitoRest.getNombre());
        return ResponseEntity.ok(animalitoRest);
    }

    @Override
    public ResponseEntity<AnimalitoRestV1DTO> crearAnimalito(DatosNuevoAnimalitoRestV1DTO datosNuevoAnimalito) {
        log.debug("Creando nuevo animalito: {}", datosNuevoAnimalito.getNombre());
        
        var datosNuevoService = mapper.toServiceDTO(datosNuevoAnimalito);
        var animalitoCreado = animalitoService.crear(datosNuevoService);
        var animalitoRest = mapper.toRestDTO(animalitoCreado);
        
        log.debug("Animalito creado con publicId: {}", animalitoRest.getPublicId());
        return ResponseEntity.status(HttpStatus.CREATED).body(animalitoRest);
    }

    @Override
    public ResponseEntity<AnimalitoRestV1DTO> modificarAnimalito(@PathVariable String publicId, DatosModificarAnimalitoRestV1DTO datosModificarAnimalito) {
        log.debug("Modificando animalito con publicId: {}", publicId);
        
        var datosModificarService = mapper.toServiceDTO(datosModificarAnimalito);
        var animalitoModificado = animalitoService.modificar(publicId, datosModificarService);
        var animalitoRest = mapper.toRestDTO(animalitoModificado);
        
        log.debug("Animalito modificado: {}", animalitoRest.getNombre());
        return ResponseEntity.ok(animalitoRest);
    }

    @Override
    public ResponseEntity<AnimalitoRestV1DTO> borrarAnimalito(@PathVariable String publicId) {
        log.debug("Eliminando animalito con publicId: {}", publicId);
        
        animalitoService.eliminar(publicId);
        
        log.debug("Animalito con publicId {} eliminado", publicId);
        return ResponseEntity.ok().build();
    }
}