package com.curso.animalitos.service.impl;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import com.curso.animalitos.service.api.dto.AnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosModificarAnimalitoServiceV1DTO;
import com.curso.animalitos.service.api.dto.DatosNuevoAnimalitoServiceV1DTO;
import com.curso.animalitos.service.impl.mapper.AnimalitoServiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
class AnimalitoServiceImplTest {
/*
    @Mock
    private AnimalitoRepository animalitoRepository;

    @Mock
    private AnimalitoServiceMapper mapper;

    private AnimalitoServiceImpl animalitoService;

    //@BeforeEach
    void setUp() {
        animalitoService = new AnimalitoServiceImpl(animalitoRepository, mapper);
    }

    //@Test
    void crear_DeberiaCrearAnimalitoCorrectamente() throws Exception {
        // Given
        DatosNuevoAnimalitoServiceV1DTO datosNuevoService = DatosNuevoAnimalitoServiceV1DTO.builder()
                .nombre("Firulais")
                .especie("Perro")
                .edad(3)
                .descripcion("Un perro muy amigable")
                .build();

        DatosNuevoAnimalito datosNuevoRepo = new DatosNuevoAnimalito(
                "Firulais", "Perro", 3, "Un perro muy amigable");

        Animalito animalitoCreado = new TestAnimalito("abc123", "Firulais", "Perro", 3, "Un perro muy amigable");

        AnimalitoServiceV1DTO expectedDto = AnimalitoServiceV1DTO.builder()
                .publicId("abc123")
                .nombre("Firulais")
                .especie("Perro")
                .edad(3)
                .descripcion("Un perro muy amigable")
                .build();

        when(mapper.toRepoDatosNuevo(datosNuevoService)).thenReturn(datosNuevoRepo);
        when(animalitoRepository.save(datosNuevoRepo)).thenReturn(animalitoCreado);
        when(mapper.toServiceDTO(animalitoCreado)).thenReturn(expectedDto);

        // When
        AnimalitoServiceV1DTO result = animalitoService.crear(datosNuevoService);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(mapper).toRepoDatosNuevo(datosNuevoService);
        verify(animalitoRepository).save(datosNuevoRepo);
        verify(mapper).toServiceDTO(animalitoCreado);
    }

    //@Test
    void obtenerTodos_DeberiaRetornarTodosLosAnimalitos() throws Exception {
        // Given
        Animalito animalito1 = new TestAnimalito("abc123", "Firulais", "Perro", 3, "Un perro muy amigable");
        Animalito animalito2 = new TestAnimalito("def456", "Whiskers", "Gato", 2, "Un gato travieso");

        AnimalitoServiceV1DTO dto1 = AnimalitoServiceV1DTO.builder()
                .publicId("abc123")
                .nombre("Firulais")
                .especie("Perro")
                .edad(3)
                .descripcion("Un perro muy amigable")
                .build();

        AnimalitoServiceV1DTO dto2 = AnimalitoServiceV1DTO.builder()
                .publicId("def456")
                .nombre("Whiskers")
                .especie("Gato")
                .edad(2)
                .descripcion("Un gato travieso")
                .build();

        when(animalitoRepository.findAll()).thenReturn(List.of(animalito1, animalito2));
        when(mapper.toServiceDTO(animalito1)).thenReturn(dto1);
        when(mapper.toServiceDTO(animalito2)).thenReturn(dto2);

        // When
        List<AnimalitoServiceV1DTO> result = animalitoService.obtenerTodos();

        // Then
        assertThat(result).containsExactly(dto1, dto2);
        verify(animalitoRepository).findAll();
        verify(mapper).toServiceDTO(animalito1);
        verify(mapper).toServiceDTO(animalito2);
    }

    //@Test
    void obtenerPorPublicId_DeberiaRetornarAnimalitoExistente() throws Exception {
        // Given
        String publicId = "abc123";
        Animalito animalito = new TestAnimalito(publicId, "Firulais", "Perro", 3, "Un perro muy amigable");

        AnimalitoServiceV1DTO expectedDto = AnimalitoServiceV1DTO.builder()
                .publicId(publicId)
                .nombre("Firulais")
                .especie("Perro")
                .edad(3)
                .descripcion("Un perro muy amigable")
                .build();

        when(animalitoRepository.findByPublicId(publicId)).thenReturn(Optional.of(animalito));
        when(mapper.toServiceDTO(animalito)).thenReturn(expectedDto);

        // When
        Optional<AnimalitoServiceV1DTO> result = animalitoService.obtenerPorPublicId(publicId);

        // Then
        assertThat(result).isPresent().contains(expectedDto);
        verify(animalitoRepository).findByPublicId(publicId);
        verify(mapper).toServiceDTO(animalito);
    }

    //@Test
    void modificar_DeberiaModificarAnimalitoCorrectamente() throws Exception {
        // Given
        String publicId = "abc123";
        DatosModificarAnimalitoServiceV1DTO datosModificarService = DatosModificarAnimalitoServiceV1DTO.builder()
                .nombre("Firulais Jr")
                .especie("Perro")
                .edad(4)
                .descripcion("Un perro muy amigable y adulto")
                .build();

        DatosModificarAnimalito datosModificarRepo = DatosModificarAnimalito.builder()
                .nombre("Firulais Jr")
                .edad(4)
                .descripcion("Un perro muy amigable y adulto")
                .build();

        Animalito animalitoModificado = new TestAnimalito(publicId, "Firulais Jr", "Perro", 4, "Un perro muy amigable y adulto");

        AnimalitoServiceV1DTO expectedDto = AnimalitoServiceV1DTO.builder()
                .publicId(publicId)
                .nombre("Firulais Jr")
                .especie("Perro")
                .edad(4)
                .descripcion("Un perro muy amigable y adulto")
                .build();

        when(mapper.toRepoDatosModificar(datosModificarService)).thenReturn(datosModificarRepo);
        when(animalitoRepository.update(publicId, datosModificarRepo)).thenReturn(animalitoModificado);
        when(mapper.toServiceDTO(animalitoModificado)).thenReturn(expectedDto);

        // When
        AnimalitoServiceV1DTO result = animalitoService.modificar(publicId, datosModificarService);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(mapper).toRepoDatosModificar(datosModificarService);
        verify(animalitoRepository).update(publicId, datosModificarRepo);
        verify(mapper).toServiceDTO(animalitoModificado);
    }

    //@Test
    void eliminar_DeberiaEliminarAnimalitoCorrectamente() throws Exception {
        // Given
        String publicId = "abc123";
        Animalito animalitoEliminado = new TestAnimalito(publicId, "Firulais", "Perro", 3, "Un perro muy amigable");
        when(animalitoRepository.deleteByPublicId(publicId)).thenReturn(Optional.of(animalitoEliminado));

        // When
        animalitoService.eliminar(publicId);

        // Then
        verify(animalitoRepository).deleteByPublicId(publicId);
    }

    // Clase helper para los tests
    private static class TestAnimalito implements Animalito {
        private final String publicId;
        private final String nombre;
        private final String especie;
        private final Integer edad;
        private final String descripcion;

        public TestAnimalito(String publicId, String nombre, String especie, Integer edad, String descripcion) {
            this.publicId = publicId;
            this.nombre = nombre;
            this.especie = especie;
            this.edad = edad;
            this.descripcion = descripcion;
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public String getNombre() {
            return nombre;
        }

        @Override
        public String getEspecie() {
            return especie;
        }

        @Override
        public Integer getEdad() {
            return edad;
        }

        @Override
        public String getDescripcion() {
            return descripcion;
        }
    }
    */
}