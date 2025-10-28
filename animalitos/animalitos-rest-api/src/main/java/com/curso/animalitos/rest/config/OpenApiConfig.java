package com.curso.animalitos.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuración OpenAPI para la documentación de la API REST de Animalitos.
 * Define la información general de la API, servidores y metadatos.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Animalitos API",
        version = "1.0.0",
        description = """
            API REST para la gestión de animalitos.
            
            Esta API permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) 
            sobre una colección de animalitos. Cada animalito tiene un identificador único, 
            nombre, especie, edad y descripción opcional.
            
            ## Características principales:
            - Gestión completa de animalitos
            - Validaciones de entrada robustas
            - Respuestas de error detalladas
            - Documentación OpenAPI completa
            
            ## Validaciones implementadas:
            - **Nombre**: Obligatorio, 2-50 caracteres, debe empezar con mayúscula
            - **Especie**: Obligatorio, 3-30 caracteres
            - **Edad**: Obligatorio, 0-1000 años
            - **Descripción**: Opcional, máximo 500 caracteres
            """,
        contact = @Contact(
            name = "Curso Spring WebFlux",
            email = "contacto@cursospringwebflux.com",
            url = "https://cursospringwebflux.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de desarrollo local"
        ),
        @Server(
            url = "https://api.animalitos.example.com",
            description = "Servidor de producción"
        )
    }
)
public class OpenApiConfig {
    // Esta clase no necesita implementación, solo las anotaciones
}