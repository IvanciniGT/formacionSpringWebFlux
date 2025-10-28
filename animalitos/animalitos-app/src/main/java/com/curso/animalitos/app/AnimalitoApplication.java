package com.curso.animalitos.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación Spring Boot para Animalitos
 */
@SpringBootApplication(scanBasePackages = "com.curso.animalitos")
@EntityScan("com.curso.animalitos.repository.jpa")
@EnableJpaRepositories("com.curso.animalitos.repository.jpa")
public class AnimalitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalitoApplication.class, args);
    }
}