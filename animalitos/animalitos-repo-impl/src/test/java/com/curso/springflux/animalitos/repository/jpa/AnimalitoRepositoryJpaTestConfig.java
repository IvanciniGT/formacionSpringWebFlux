package com.curso.animalitos.repository.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Configuración de test para los tests de repositorio JPA.
 * 
 * La anotación @SpringBootApplication es una anotación de conveniencia que combina:
 * - @SpringBootConfiguration: Marca esta clase como configuración de Spring Boot
 * - @EnableAutoConfiguration: Habilita la autoconfiguración automática de Spring Boot
 * - @ComponentScan: Escanea automáticamente componentes en el package actual y subpackages
 * 
 * Esta anotación hace EVERYTHING automáticamente:
 * - Detecta entidades JPA (@Entity)
 * - Configura repositorios Spring Data JPA
 * - Configura la base de datos H2 para tests
 * - Habilita transacciones
 * - Configura el EntityManager y DataSource
 * 
 * ¡Es mucho más simple que configurar manualmente!
 */
@SpringBootApplication
public class AnimalitoRepositoryJpaTestConfig {
    
    /**
     * Método main opcional para poder ejecutar esta configuración como aplicación independiente.
     * No es necesario para los tests, pero puede ser útil para debugging.
     */
    public static void main(String[] args) {
        SpringApplication.run(AnimalitoRepositoryJpaTestConfig.class, args);
    }
}

/*
 * CONFIGURACIÓN MANUAL COMENTADA (La manera complicada que estaba usando antes):
 * 
 * @SpringBootConfiguration      // Equivale a @Configuration + metadatos de Spring Boot
 * @EnableAutoConfiguration      // Autoconfiguración automática basada en dependencias del classpath
 * @ComponentScan(basePackages = "com.curso.animalitos.repository.jpa")  // Escaneo de componentes
 * @EntityScan(basePackages = "com.curso.animalitos.repository.jpa")     // Escaneo de entidades JPA
 * @EnableJpaRepositories(basePackages = "com.curso.animalitos.repository.jpa") // Habilita repos JPA
 * 
 * ¡Todas estas anotaciones las hace @SpringBootApplication automáticamente de manera inteligente!
 */