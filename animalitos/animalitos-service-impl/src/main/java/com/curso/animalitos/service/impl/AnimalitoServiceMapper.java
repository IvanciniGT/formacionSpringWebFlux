package com.curso.animalitos.service.impl.mapper;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimalitoServiceMapper {

    /**
     * Convierte el DTO de servicio a la interfaz del repositorio
     */
    AnimalitoServiceV1DTO toServiceDTO(Animalito animalito);

    /**
     * Convierte los datos de nuevo animalito del servicio al repositorio
     */
    DatosNuevoAnimalito toRepoDatosNuevo(DatosNuevoAnimalitoServiceV1DTO datosNuevoService);

    /**
     * Convierte los datos de modificación del servicio al repositorio
     */
    DatosModificarAnimalito toRepoDatosModificar(DatosModificarAnimalitoServiceV1DTO datosModificarService);
}