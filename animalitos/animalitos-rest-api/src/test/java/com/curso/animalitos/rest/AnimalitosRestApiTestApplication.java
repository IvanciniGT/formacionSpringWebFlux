package com.curso.animalitos.rest;

import com.curso.animalitos.rest.AnimalitosRestV1Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Aplicación Spring Boot para testing y generación de documentación OpenAPI.
 * Esta aplicación se usa únicamente durante los tests de integración
 * para levantar un contexto completo con mock dummy y generar la documentación automática.
 */
@SpringBootApplication
public class AnimalitosRestApiTestApplication {

    /**
     * Mock bean dummy del controller para generar la documentación OpenAPI.
     */
    @MockBean
    private AnimalitosRestV1Controller animalitosRestV1Controller;

    public static void main(String[] args) {
        SpringApplication.run(AnimalitosRestApiTestApplication.class, args);
    }
}