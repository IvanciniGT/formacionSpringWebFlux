# Animalitos REST API

Este módulo contiene la definición de la API REST para la gestión de animalitos - Versión 1.

## Características

- **DTOs** con validaciones y documentación OpenAPI
- **Interfaz del controlador** completamente documentada
- **Generación automática** de documentación OpenAPI
- **Validaciones robustas** usando anotaciones custom
- **Respuestas de error estandarizadas**

## Estructura

```
src/main/java/
└── com/curso/springflux/animalitos/rest/
    ├── config/
    │   └── OpenApiConfig.java          # Configuración global OpenAPI
    ├── controller/
    │   └── AnimalitosRestV1Controller.java # Interfaz del controlador REST
    └── dto/
        ├── AnimalitoRestV1DTO.java     # DTO completo del animalito
        ├── DatosNuevoAnimalitoRestV1DTO.java    # DTO para crear animalito
        └── DatosModificarAnimalitoRestV1DTO.java # DTO para modificar animalito
```

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/animalitos` | Listar todos los animalitos |
| GET | `/api/v1/animalitos/{publicId}` | Obtener animalito por ID |
| POST | `/api/v1/animalitos` | Crear nuevo animalito |
| PUT | `/api/v1/animalitos/{publicId}` | Modificar animalito existente |
| DELETE | `/api/v1/animalitos/{publicId}` | Eliminar animalito |

## Validaciones

### Nombre
- Obligatorio, no nulo, no vacío
- Longitud: 2-50 caracteres
- Debe comenzar con mayúscula
- Anotación: `@NombreAnimalitoValido`

### Especie
- Obligatorio, no nulo, no vacío
- Longitud: 3-30 caracteres
- Anotación: `@EspecieAnimalitoValida`

### Edad
- Obligatorio, no nulo
- Valor: 0-1000 años
- Anotación: `@EdadAnimalitoValida`

### Descripción
- Opcional
- Longitud máxima: 500 caracteres
- Valor por defecto: cadena vacía
- Anotación: `@DescripcionAnimalitoValida`

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200 | Operación exitosa |
| 201 | Recurso creado exitosamente |
| 400 | Datos de entrada inválidos |
| 404 | Recurso no encontrado |
| 500 | Error interno del servidor |

## Generación de Documentación

La documentación OpenAPI se genera automáticamente con el plugin de Maven:

```bash
mvn clean verify
```

El archivo `openapi.json` se genera en `target/openapi/openapi.json`.

## Dependencias

- **Spring Web**: Anotaciones REST
- **SpringDoc OpenAPI**: Documentación automática
- **Jakarta Validation**: Validaciones
- **Lombok**: Reducción de boilerplate
- **Animalitos Common Utils**: Validaciones custom

## Próximos Pasos

1. Implementación del controlador REST (módulo `animalitos-rest-impl`)
2. Mappers entre DTOs y entidades de servicio
3. Tests unitarios y de integración
4. Manejo de errores con ControllerAdvice