package com.curso.animalitos.repository.jpa;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.validations.DescripcionAnimalitoValida;
import com.curso.animalitos.validations.EdadAnimalitoValida;
import com.curso.animalitos.validations.EspecieAnimalitoValida;
import com.curso.animalitos.validations.NombreAnimalitoValido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entidad JPA que representa un animalito en la base de datos.
 * Implementa la interfaz Animalito del API del repositorio.
 */
@Entity
@Table(name = "animalitos", indexes = {
        @Index(name = "idx_animalito_public_id", columnList = "public_id", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalitoEntity implements Animalito {
    
    /**
     * ID interno autoincremental (NO se expone fuera de la capa de persistencia)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * ID público (UUID v4) que se expone a las capas superiores
     */
    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;
    
    /**
     * Nombre del animalito
     */
    @Column(name = "nombre", nullable = false, length = 50)
    @NotNull
    @NombreAnimalitoValido
    private String nombre;
    
    /**
     * Especie del animalito
     */
    @Column(name = "especie", nullable = false, length = 30)
    @NotNull
    @EspecieAnimalitoValida
    private String especie;
    
    /**
     * Edad del animalito en años
     */
    @Column(name = "edad", nullable = false)
    @NotNull
    @EdadAnimalitoValida
    private Integer edad;
    
    /**
     * Descripción del animalito
     */
    @Column(name = "descripcion", length = 500)
    @DescripcionAnimalitoValida
    private String descripcion;
    
    /**
     * Callback que se ejecuta antes de persistir para generar el publicId
     * y establecer valores por defecto
     */
    @PrePersist
    private void prepararParaPersistencia() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID().toString();
        }
        if (this.descripcion == null) {
            this.descripcion = "";
        }
    }
    
    // Los getters de la interfaz Animalito se generan automáticamente por Lombok @Data
}