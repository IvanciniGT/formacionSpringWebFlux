package com.curso.animalitos.service.impl;

import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import com.curso.animalitos.repository.AnimalitosRepositoryException;
import com.curso.animalitos.service.api.AnimalitoService;
import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.exception.AnimalitoNoEncontradoException;
import com.curso.animalitos.service.api.exception.ErrorValidacionException;
import com.curso.animalitos.service.impl.mapper.AnimalitoServiceMapper;
import org.springframework.stereotype.Service;

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
    public AnimalitoServiceV1DTO crear(DatosNuevoAnimalitoServiceV1DTO datosNuevo) {
        try {
            DatosNuevoAnimalito datosRepo = mapper.toRepoDatosNuevo(datosNuevo);
            return mapper.toServiceDTO(animalitoRepository.save(datosRepo));
        } catch (AnimalitosRepositoryException e) {
            throw new ErrorValidacionException("Error al crear animalito: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AnimalitoServiceV1DTO> obtenerTodos() {
        try {
            return animalitoRepository.findAll().stream()
                    .map(mapper::toServiceDTO)
                    .toList();
        } catch (AnimalitosRepositoryException e) {
            throw new RuntimeException("Error al obtener animalitos: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AnimalitoServiceV1DTO> obtenerPorPublicId(String publicId) {
        try {
            return animalitoRepository.findByPublicId(publicId)
                    .map(mapper::toServiceDTO);
        } catch (AnimalitosRepositoryException e) {
            throw new RuntimeException("Error al obtener animalito: " + e.getMessage(), e);
        }
    }

    @Override
    public AnimalitoServiceV1DTO modificar(String publicId, DatosModificarAnimalitoServiceV1DTO datosModificar) {
        try {
            DatosModificarAnimalito datosRepo = mapper.toRepoDatosModificar(datosModificar);
            return mapper.toServiceDTO(animalitoRepository.update(publicId, datosRepo));
        } catch (AnimalitosRepositoryException e) {
            // Si el error es porque no encontró el animalito, lanzar excepción específica
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")) {
                throw new AnimalitoNoEncontradoException("Animalito no encontrado con ID: " + publicId, e);
            }
            throw new ErrorValidacionException("Error al modificar animalito: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String publicId) {
        try {
            Optional<com.curso.animalitos.repository.Animalito> eliminated = 
                animalitoRepository.deleteByPublicId(publicId);
            if (eliminated.isEmpty()) {
                throw new AnimalitoNoEncontradoException("Animalito no encontrado con ID: " + publicId);
            }
        } catch (AnimalitosRepositoryException e) {
            throw new RuntimeException("Error al eliminar animalito: " + e.getMessage(), e);
        }
    }
}