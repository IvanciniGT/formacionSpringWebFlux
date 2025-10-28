package com.curso.animalitos.service.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnimalitoServiceV1DTO {
    String publicId;
    String nombre;
    String especie;
    Integer edad;
    String descripcion;
}