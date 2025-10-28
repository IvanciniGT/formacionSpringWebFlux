package com.curso.animalitos.service.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DatosModificarAnimalitoServiceV1DTO {
    String nombre;
    String especie;
    Integer edad;
    String descripcion;
}