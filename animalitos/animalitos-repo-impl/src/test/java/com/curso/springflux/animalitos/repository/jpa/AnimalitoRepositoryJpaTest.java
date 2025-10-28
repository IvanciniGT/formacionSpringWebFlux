package com.curso.animalitos.repository.jpa;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.repository.AnimalitoRepository;
import com.curso.animalitos.repository.AnimalitosRepositoryException;
import com.curso.animalitos.repository.DatosModificarAnimalito;
import com.curso.animalitos.repository.DatosNuevoAnimalito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios EXHAUSTIVOS para AnimalitoRepositoryJpa siguiendo principios FIRST:
 * - Fast: Tests rápidos con H2 en memoria
 * - Independent: Cada test es independiente  
 * - Repeatable: Se pueden ejecutar en cualquier entorno
 * - Self-validating: Pass/Fail sin intervención manual
 * - Timely: Escritos antes de la implementación (TDD)
 * 
 * COBERTURA COMPLETA:
 * - CRUD básico (Create, Read, Update, Delete)
 * - Casos exitosos y casos de error
 * - Validaciones exhaustivas de todas las anotaciones custom
 * - Casos edge y de integridad
 */

// mvn test. Maven ejecuta la prueba? Maven no sabe hacer la o con un canuto. To do lo delega en plugins
// Quién ejecuta las pruebas? El plugin surefire
// Y ese plugin sabe pedir purbeas a JUNIT.
// Quien ejecuta las pruebas es JUNIT
// JUnit es quien va a crear la instancia de esta clase de prueba : new AnimalitoRepositoryJpaTest()
// Y JUnit... sabe? de donde sacar ese dato? NPI
// Para darle una pista a JUNIT, que ese paarámetro debe pedirlo a Spring, le ponemos @Autowired
@SpringBootTest(classes = {AnimalitoRepositoryJpaTestConfig.class})
@TestPropertySource("classpath:application-test.properties")
@Transactional
class AnimalitoRepositoryJpaTest {

    private final AnimalitoRepository animalitoRepository;

    // Este si es un uso legítimo y necesario de @Autowired
    public AnimalitoRepositoryJpaTest(@Autowired AnimalitoRepository animalitoRepository){
        this.animalitoRepository = animalitoRepository;
    }

    @Test
    void deberiaEliminarAnimalitoExistentePorPublicId() throws AnimalitosRepositoryException {
        // DADO: Datos válidos de un animalito
        DatosNuevoAnimalito datosAnimalito = new DatosNuevoAnimalito(
            "Firulais", 
            "Perro", 
            5, 
            "Un perro muy juguetón"
        );
        
        // Y DADO: Que guardo previamente ese animalito en el repositorio
        Animalito animalitoGuardado = animalitoRepository.save(datosAnimalito);
        
        // Y DADO: Que capturo el publicId generado por el repositorio
        String publicId = animalitoGuardado.getPublicId();
        
        // CUANDO: Pido que se borre el animalito por su publicId
        Optional<Animalito> animalitoEliminado = animalitoRepository.deleteByPublicId(publicId);
        
        // ENTONCES: Compruebo que el animalito ya no existe en el repositorio
        Optional<Animalito> animalitoEncontrado = animalitoRepository.findByPublicId(publicId);
        assertThat(animalitoEncontrado).isEmpty();
        
        // Y me aseguro que el animalito eliminado es el que me ha sido devuelto con todos sus datos
        assertThat(animalitoEliminado).isPresent();
        Animalito eliminado = animalitoEliminado.get();
        
        assertThat(eliminado.getPublicId()).isEqualTo(publicId);
        assertThat(eliminado.getNombre()).isEqualTo("Firulais");
        assertThat(eliminado.getEspecie()).isEqualTo("Perro");
        assertThat(eliminado.getEdad()).isEqualTo(5);
        assertThat(eliminado.getDescripcion()).isEqualTo("Un perro muy juguetón");
    }
    
    @Test
    void deberiaRetornarVacioAlEliminarAnimalitoInexistente() throws AnimalitosRepositoryException {
        // DADO: Un publicId que no existe en el repositorio
        String publicIdInexistente = "550e8400-e29b-41d4-a716-446655440000";
        
        // CUANDO: Intento eliminar ese animalito
        Optional<Animalito> resultado = animalitoRepository.deleteByPublicId(publicIdInexistente);
        
        // ENTONCES: El resultado debe estar vacío
        assertThat(resultado).isEmpty();
    }
    
    @Test
    void deberiaGuardarNuevoAnimalitoYGenerarPublicId() throws AnimalitosRepositoryException {
        // DADO: Datos válidos de un nuevo animalito
        DatosNuevoAnimalito datosAnimalito = new DatosNuevoAnimalito(
            "Michi", 
            "Gato", 
            3
        );
        
        // CUANDO: Guardo el animalito en el repositorio
        Animalito animalitoGuardado = animalitoRepository.save(datosAnimalito);
        
        // ENTONCES: El animalito debe tener un publicId generado
        assertThat(animalitoGuardado.getPublicId()).isNotNull();
        assertThat(animalitoGuardado.getPublicId()).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        
        // Y debe contener todos los datos proporcionados
        assertThat(animalitoGuardado.getNombre()).isEqualTo("Michi");
        assertThat(animalitoGuardado.getEspecie()).isEqualTo("Gato");
        assertThat(animalitoGuardado.getEdad()).isEqualTo(3);
        assertThat(animalitoGuardado.getDescripcion()).isEqualTo(""); // Valor por defecto
    }
    
    @Test
    void deberiaEncontrarAnimalitosPorPublicId() throws AnimalitosRepositoryException {
        // DADO: Un animalito guardado en el repositorio
        DatosNuevoAnimalito datosAnimalito = new DatosNuevoAnimalito(
            "Rex", 
            "Pastor Alemán", 
            7, 
            "Un perro guardián muy leal"
        );
        Animalito animalitoGuardado = animalitoRepository.save(datosAnimalito);
        String publicId = animalitoGuardado.getPublicId();
        
        // CUANDO: Busco el animalito por su publicId
        Optional<Animalito> animalitoEncontrado = animalitoRepository.findByPublicId(publicId);
        
        // ENTONCES: Debe encontrar el animalito con todos sus datos
        assertThat(animalitoEncontrado).isPresent();
        Animalito encontrado = animalitoEncontrado.get();
        
        assertThat(encontrado.getPublicId()).isEqualTo(publicId);
        assertThat(encontrado.getNombre()).isEqualTo("Rex");
        assertThat(encontrado.getEspecie()).isEqualTo("Pastor Alemán");
        assertThat(encontrado.getEdad()).isEqualTo(7);
        assertThat(encontrado.getDescripcion()).isEqualTo("Un perro guardián muy leal");

        // Con sintaxis standard de JUNIT: SOLO ES UNA CUESTION DE ESTILO
        assertTrue(animalitoEncontrado.isPresent());
        assertEquals(publicId, animalitoEncontrado.get().getPublicId());
        assertEquals("Rex", animalitoEncontrado.get().getNombre());
        assertEquals("Pastor Alemán", animalitoEncontrado.get().getEspecie());
        assertEquals(7, animalitoEncontrado.get().getEdad());
        assertEquals("Un perro guardián muy leal", animalitoEncontrado.get().getDescripcion());
    }
    
    @Test
    void deberiaRetornarTodosLosAnimalitos() throws AnimalitosRepositoryException {
        // DADO: Varios animalitos guardados en el repositorio
        DatosNuevoAnimalito animalito1 = new DatosNuevoAnimalito("Buddy", "Golden Retriever", 4);
        DatosNuevoAnimalito animalito2 = new DatosNuevoAnimalito("Whiskers", "Gato Persa", 2);
        DatosNuevoAnimalito animalito3 = new DatosNuevoAnimalito("Tweety", "Canario", 1);
        
        animalitoRepository.save(animalito1);
        animalitoRepository.save(animalito2);
        animalitoRepository.save(animalito3);
        
        // CUANDO: Pido todos los animalitos
        List<Animalito> todosLosAnimalitos = animalitoRepository.findAll();
        
        // ENTONCES: Debe devolver al menos los 3 animalitos guardados
        assertThat(todosLosAnimalitos).hasSizeGreaterThanOrEqualTo(3);
        assertThat(todosLosAnimalitos).extracting(Animalito::getNombre)
            .contains("Buddy", "Whiskers", "Tweety");
    }

    // =======================================
    // TESTS EXHAUSTIVOS - CASOS FALTANTES
    // =======================================

    @Test
    void deberiaRetornarVacioAlBuscarAnimalitoInexistente() throws AnimalitosRepositoryException {
        // DADO: Un publicId que no existe en el repositorio
        String publicIdInexistente = "550e8400-e29b-41d4-a716-446655440999";
        
        // CUANDO: Busco ese animalito
        Optional<Animalito> resultado = animalitoRepository.findByPublicId(publicIdInexistente);
        
        // ENTONCES: El resultado debe estar vacío
        assertThat(resultado).isEmpty();
    }

    @Test
    void deberiaActualizarAnimalitoExistente() throws AnimalitosRepositoryException {
        // DADO: Un animalito guardado en el repositorio
        DatosNuevoAnimalito datosOriginales = new DatosNuevoAnimalito(
            "Luna", "Gato Siamés", 2, "Gata muy cariñosa"
        );
        Animalito animalitoGuardado = animalitoRepository.save(datosOriginales);
        String publicId = animalitoGuardado.getPublicId();
        
        // Y DADO: Nuevos datos para actualizar
        DatosModificarAnimalito datosActualizados = DatosModificarAnimalito.builder()
            .nombre("Luna Actualizada")
            .edad(3)
            .descripcion("Gata muy cariñosa y juguetona")
            .build();
        
        // CUANDO: Actualizo el animalito
        Animalito animalitoActualizado = animalitoRepository.update(publicId, datosActualizados);
        
        // ENTONCES: Los datos deben estar actualizados
        assertThat(animalitoActualizado.getPublicId()).isEqualTo(publicId);
        assertThat(animalitoActualizado.getNombre()).isEqualTo("Luna Actualizada");
        assertThat(animalitoActualizado.getEspecie()).isEqualTo("Gato Siamés"); // La especie NO cambia
        assertThat(animalitoActualizado.getEdad()).isEqualTo(3);
        assertThat(animalitoActualizado.getDescripcion()).isEqualTo("Gata muy cariñosa y juguetona");
        
        // Y ADEMÁS: Al buscar por ID debe devolver los datos actualizados
        Optional<Animalito> animalitoEncontrado = animalitoRepository.findByPublicId(publicId);
        assertThat(animalitoEncontrado).isPresent();
        assertThat(animalitoEncontrado.get().getNombre()).isEqualTo("Luna Actualizada");
        assertThat(animalitoEncontrado.get().getEdad()).isEqualTo(3);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarAnimalitoInexistente() {
        // DADO: Un publicId que no existe en el repositorio
        String publicIdInexistente = "550e8400-e29b-41d4-a716-446655440888";
        
        // Y DADO: Datos válidos para actualizar
        DatosModificarAnimalito datosActualizados = DatosModificarAnimalito.builder()
            .nombre("Nombre Inexistente")
            .edad(5)
            .descripcion("Descripción inexistente")
            .build();
        
        // CUANDO/ENTONCES: Intento actualizar debe lanzar excepción
        assertThatThrownBy(() -> animalitoRepository.update(publicIdInexistente, datosActualizados))
            .isInstanceOf(AnimalitosRepositoryException.class)
            .hasMessageContaining("No se encontró animalito con publicId");
    }

    @Test
    void deberiaRetornarListaVaciaEnRepositorioVacio() throws AnimalitosRepositoryException {
        // DADO: Un repositorio limpio (garantizado por @Transactional)
        
        // CUANDO: Pido todos los animalitos
        List<Animalito> todosLosAnimalitos = animalitoRepository.findAll();
        
        // ENTONCES: Debe devolver lista vacía
        assertThat(todosLosAnimalitos).isEmpty();
    }

    @Test
    void deberiaGenerarPublicIdsUnicos() throws AnimalitosRepositoryException {
        // DADO: Múltiples animalitos a guardar
        DatosNuevoAnimalito animalito1 = new DatosNuevoAnimalito("Max", "Perro", 3);
        DatosNuevoAnimalito animalito2 = new DatosNuevoAnimalito("Max", "Perro", 3); // Mismos datos
        DatosNuevoAnimalito animalito3 = new DatosNuevoAnimalito("Max", "Perro", 3); // Mismos datos
        
        // CUANDO: Guardo múltiples animalitos
        Animalito guardado1 = animalitoRepository.save(animalito1);
        Animalito guardado2 = animalitoRepository.save(animalito2);
        Animalito guardado3 = animalitoRepository.save(animalito3);
        
        // ENTONCES: Cada uno debe tener un publicId único
        Set<String> publicIds = new HashSet<>();
        publicIds.add(guardado1.getPublicId());
        publicIds.add(guardado2.getPublicId());
        publicIds.add(guardado3.getPublicId());
        
        assertThat(publicIds).hasSize(3); // Todos diferentes
        assertThat(guardado1.getPublicId()).isNotEqualTo(guardado2.getPublicId());
        assertThat(guardado2.getPublicId()).isNotEqualTo(guardado3.getPublicId());
        assertThat(guardado1.getPublicId()).isNotEqualTo(guardado3.getPublicId());
    }

    // =======================================
    // TESTS DE VALIDACIONES EXHAUSTIVAS
    // =======================================

    @Test
    void deberiaLanzarExcepcionAlGuardarAnimalitoConNombreInvalido() {
        // CASOS INVÁLIDOS para @NombreAnimalitoValido
        
        // CASO: Nombre null
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosNombreNull = new DatosNuevoAnimalito(null, "Perro", 5);
            animalitoRepository.save(datosNombreNull);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("nombre");
        
        // CASO: Nombre vacío
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosNombreVacio = new DatosNuevoAnimalito("", "Perro", 5);
            animalitoRepository.save(datosNombreVacio);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("nombre");
        
        // CASO: Nombre solo espacios
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosNombreSoloEspacios = new DatosNuevoAnimalito("   ", "Perro", 5);
            animalitoRepository.save(datosNombreSoloEspacios);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("nombre");
        
        // CASO: Nombre demasiado largo (>50 caracteres)
        assertThatThrownBy(() -> {
            String nombreMuyLargo = "a".repeat(51);
            DatosNuevoAnimalito datosNombreLargo = new DatosNuevoAnimalito(nombreMuyLargo, "Perro", 5);
            animalitoRepository.save(datosNombreLargo);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("nombre");
    }

    @Test
    void deberiaLanzarExcepcionAlGuardarAnimalitoConEspecieInvalida() {
        // CASOS INVÁLIDOS para @EspecieAnimalitoValida
        
        // CASO: Especie null
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosEspecieNull = new DatosNuevoAnimalito("Firulais", null, 5);
            animalitoRepository.save(datosEspecieNull);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("especie");
        
        // CASO: Especie vacía
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosEspecieVacia = new DatosNuevoAnimalito("Firulais", "", 5);
            animalitoRepository.save(datosEspecieVacia);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("especie");
        
        // CASO: Especie demasiado larga (>30 caracteres)
        assertThatThrownBy(() -> {
            String especieMuyLarga = "a".repeat(31);
            DatosNuevoAnimalito datosEspecieLarga = new DatosNuevoAnimalito("Firulais", especieMuyLarga, 5);
            animalitoRepository.save(datosEspecieLarga);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("especie");
    }

    @Test
    void deberiaLanzarExcepcionAlGuardarAnimalitoConEdadInvalida() {
        // CASOS INVÁLIDOS para @EdadAnimalitoValida
        
        // CASO: Edad null - validación Jakarta (now with @NotNull in EdadAnimalitoValida)
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosEdadNull = new DatosNuevoAnimalito("Firulais", "Perro", null);
            animalitoRepository.save(datosEdadNull);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("edad");
        
        // CASO: Edad negativa - validación Jakarta
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosEdadNegativa = new DatosNuevoAnimalito("Firulais", "Perro", -1);
            animalitoRepository.save(datosEdadNegativa);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("edad");
        
        // CASO: Edad demasiado alta (>1000 años) - validación Jakarta
        assertThatThrownBy(() -> {
            DatosNuevoAnimalito datosEdadExcesiva = new DatosNuevoAnimalito("Firulais", "Perro", 1001);
            animalitoRepository.save(datosEdadExcesiva);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("edad");
    }

    @Test
    void deberiaLanzarExcepcionAlGuardarAnimalitoConDescripcionInvalida() {
        // CASOS INVÁLIDOS para @DescripcionAnimalitoValida
        
        // CASO: Descripción demasiado larga (>500 caracteres)
        assertThatThrownBy(() -> {
            String descripcionMuyLarga = "a".repeat(501);
            DatosNuevoAnimalito datosDescripcionLarga = new DatosNuevoAnimalito("Firulais", "Perro", 5, descripcionMuyLarga);
            animalitoRepository.save(datosDescripcionLarga);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("descripcion");
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarAnimalitoConDatosInvalidos() throws AnimalitosRepositoryException {
        // DADO: Un animalito válido en el repositorio
        DatosNuevoAnimalito datosValidos = new DatosNuevoAnimalito("Rocky", "Perro", 6);
        Animalito animalitoGuardado = animalitoRepository.save(datosValidos);
        String publicId = animalitoGuardado.getPublicId();
        
        // CASOS INVÁLIDOS para UPDATE
        
        // CASO: Nombre inválido en actualización
        assertThatThrownBy(() -> {
            DatosModificarAnimalito datosNombreInvalido = DatosModificarAnimalito.builder()
                .nombre("")  // Nombre vacío
                .edad(7)
                .descripcion("Descripción válida")
                .build();
            animalitoRepository.update(publicId, datosNombreInvalido);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("nombre");
        
        // CASO: Edad inválida en actualización
        assertThatThrownBy(() -> {
            DatosModificarAnimalito datosEdadInvalida = DatosModificarAnimalito.builder()
                .nombre("Rocky Actualizado")
                .edad(-5)  // Edad negativa
                .descripcion("Descripción válida")
                .build();
            animalitoRepository.update(publicId, datosEdadInvalida);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("edad");
        
        // CASO: Descripción inválida en actualización
        assertThatThrownBy(() -> {
            String descripcionMuyLarga = "a".repeat(501);
            DatosModificarAnimalito datosDescripcionInvalida = DatosModificarAnimalito.builder()
                .nombre("Rocky Actualizado")
                .edad(7)
                .descripcion(descripcionMuyLarga)  // Descripción muy larga
                .build();
            animalitoRepository.update(publicId, datosDescripcionInvalida);
        }).isInstanceOf(ConstraintViolationException.class)
          .hasMessageContaining("descripcion");
    }

    @Test
    void deberiaMantenerIntegridadDeDatosEnOperacionesConsecutivas() throws AnimalitosRepositoryException {
        // DADO: Un animalito inicial
        DatosNuevoAnimalito datosIniciales = new DatosNuevoAnimalito("Bella", "Gato", 4, "Gata hermosa");
        Animalito animalitoOriginal = animalitoRepository.save(datosIniciales);
        String publicId = animalitoOriginal.getPublicId();
        
        // CUANDO: Realizo múltiples operaciones
        
        // 1. Verifico que se puede encontrar
        Optional<Animalito> encontrado1 = animalitoRepository.findByPublicId(publicId);
        assertThat(encontrado1).isPresent();
        assertThat(encontrado1.get().getNombre()).isEqualTo("Bella");
        
        // 2. Actualizo el animalito
        DatosModificarAnimalito datosActualizados = DatosModificarAnimalito.builder()
            .nombre("Bella Modificada")
            .edad(5)
            .descripcion("Gata hermosa y modificada")
            .build();
        animalitoRepository.update(publicId, datosActualizados);
        
        // 3. Verifico que la actualización persistió
        Optional<Animalito> encontrado2 = animalitoRepository.findByPublicId(publicId);
        assertThat(encontrado2).isPresent();
        assertThat(encontrado2.get().getNombre()).isEqualTo("Bella Modificada");
        assertThat(encontrado2.get().getEdad()).isEqualTo(5);
        assertThat(encontrado2.get().getEspecie()).isEqualTo("Gato"); // La especie NO debe cambiar
        
        // 4. Elimino el animalito
        Optional<Animalito> animalitoEliminado = animalitoRepository.deleteByPublicId(publicId);
        assertThat(animalitoEliminado).isPresent();
        assertThat(animalitoEliminado.get().getNombre()).isEqualTo("Bella Modificada");
        
        // 5. Verifico que ya no se puede encontrar
        Optional<Animalito> encontrado3 = animalitoRepository.findByPublicId(publicId);
        assertThat(encontrado3).isEmpty();
        
        // ENTONCES: Todas las operaciones deben mantener integridad de datos
        // (Verificado implícitamente en los asserts anteriores)
    }

    @Test  
    void deberiaAceptarValoresValidosEnLimites() throws AnimalitosRepositoryException {
        // CASOS VÁLIDOS en los límites de las validaciones
        
        // CASO: Valores mínimos válidos
        DatosNuevoAnimalito datosMinimos = new DatosNuevoAnimalito("Max", "Cat", 0, "");
        Animalito animalitoMinimo = animalitoRepository.save(datosMinimos);
        assertThat(animalitoMinimo.getNombre()).isEqualTo("Max");
        assertThat(animalitoMinimo.getEspecie()).isEqualTo("Cat");
        assertThat(animalitoMinimo.getEdad()).isEqualTo(0);
        assertThat(animalitoMinimo.getDescripcion()).isEqualTo("");
        
        // CASO: Valores máximos válidos
        String nombreMaximo = "A".repeat(50);  // 50 caracteres exactos
        String especieMaxima = "b".repeat(30); // 30 caracteres exactos
        String descripcionMaxima = "c".repeat(500); // 500 caracteres exactos
        
        DatosNuevoAnimalito datosMaximos = new DatosNuevoAnimalito(
            nombreMaximo, especieMaxima, 100, descripcionMaxima
        );
        Animalito animalitoMaximo = animalitoRepository.save(datosMaximos);
        assertThat(animalitoMaximo.getNombre()).isEqualTo(nombreMaximo);
        assertThat(animalitoMaximo.getEspecie()).isEqualTo(especieMaxima);
        assertThat(animalitoMaximo.getEdad()).isEqualTo(100);
        assertThat(animalitoMaximo.getDescripcion()).isEqualTo(descripcionMaxima);
    }
}