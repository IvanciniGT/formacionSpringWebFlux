package com.curso.animalitos.repository.jpa;

import com.curso.animalitos.repository.Animalito;
import com.curso.animalitos.validations.DescripcionAnimalitoValida;
import com.curso.animalitos.validations.EdadAnimalitoValida;
import com.curso.animalitos.validations.EspecieAnimalitoValida;
import com.curso.animalitos.validations.NombreAnimalitoValido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "animalitos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalitoEntity implements Animalito {
    
    /**
     * ID interno autoincremental (NO se expone fuera de la capa de persistencia)
     */
    @Id
    @Column("id")
    private Long id;
    
    /**
     * ID público (UUID v4) que se expone a las capas superiores
     */
    @Column("public_id")
    private String publicId;
    
    /**
     * Nombre del animalito
     */
    @Column("nombre")
    @NotNull
    @NombreAnimalitoValido
    private String nombre;
    
    /**
     * Especie del animalito
     */
    @Column("especie")
    @NotNull
    @EspecieAnimalitoValida
    private String especie;
    
    /**
     * Edad del animalito en años
     */
    @Column("edad")
    @NotNull
    @EdadAnimalitoValida
    private Integer edad;
    
    /**
     * Descripción del animalito
     */
    @Column("descripcion")
    @DescripcionAnimalitoValida
    private String descripcion;

    
}