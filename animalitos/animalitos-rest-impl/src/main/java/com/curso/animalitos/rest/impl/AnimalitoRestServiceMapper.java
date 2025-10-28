package com.curso.animalitos.rest.impl;

import com.curso.animalitos.rest.dto.AnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosModificarAnimalitoRestV1DTO;
import com.curso.animalitos.rest.dto.DatosNuevoAnimalitoRestV1DTO;
import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper entre DTOs de REST y DTOs de Service
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimalitoRestServiceMapper {

    /**
     * Convierte un DTO de REST a DTO de Service
     */
    AnimalitoServiceV1DTO toServiceDTO(AnimalitoRestV1DTO restDTO);

    /**
     * Convierte un DTO de Service a DTO de REST
     */
    AnimalitoRestV1DTO toRestDTO(AnimalitoServiceV1DTO serviceDTO);

    /**
     * Convierte una lista de DTOs de Service a DTOs de REST
     */
    List<AnimalitoRestV1DTO> toRestDTOList(List<AnimalitoServiceV1DTO> serviceDTOList);

    /**
     * Convierte datos de nuevo animalito de REST a Service
     */
    DatosNuevoAnimalitoServiceV1DTO toServiceDTO(DatosNuevoAnimalitoRestV1DTO restDTO);

    /**
     * Convierte datos de modificar animalito de REST a Service
     */
    DatosModificarAnimalitoServiceV1DTO toServiceDTO(DatosModificarAnimalitoRestV1DTO restDTO);
}