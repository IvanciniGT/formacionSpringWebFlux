package com.curso.animalitos.app;

import com.curso.animalitos.rest.impl.AnimalitoRestV1ControllerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Clase principal de la aplicación Spring Boot para Animalitos
 */
@SpringBootApplication(scanBasePackages = "com.curso.animalitos")
@ComponentScan(basePackages = "com.curso.animalitos")
@ComponentScan(basePackages = "com.curso.animalitos", basePackageClasses = AnimalitoRestV1ControllerImpl.class)
@EntityScan("com.curso.animalitos.repository.r2dbc")
@EnableR2dbcRepositories("com.curso.animalitos.repository.r2dbc")
public class AnimalitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalitoApplication.class, args);
    }
}