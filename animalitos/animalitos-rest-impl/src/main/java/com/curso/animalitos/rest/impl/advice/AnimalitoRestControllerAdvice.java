package com.curso.animalitos.rest.impl.advice;

import com.curso.animalitos.rest.dto.ErrorResponseRestV1DTO;
import com.curso.animalitos.service.api.exception.ErrorValidacionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Controlador global de excepciones para la capa REST
 * Implementa el patrón AOP para manejo centralizado de errores
 */
@RestControllerAdvice
@Slf4j
public class AnimalitoRestControllerAdvice {


    @ExceptionHandler(ErrorValidacionException.class)
    public ResponseEntity<ErrorResponseRestV1DTO> manejarErrorValidacion(
            ErrorValidacionException ex, 
            WebRequest request) {
        
        log.warn("Error de validación: {}", ex.getMessage());
        
        var errorResponse = new ErrorResponseRestV1DTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseRestV1DTO> manejarValidacionBean(
            MethodArgumentNotValidException ex, 
            WebRequest request) {
        
        log.warn("Error de validación Bean: {}", ex.getMessage());
        
        var errorResponse = new ErrorResponseRestV1DTO(
                HttpStatus.BAD_REQUEST.value(),
                "Datos de entrada no válidos: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseRestV1DTO> manejarErrorGenerico(
            Exception ex, 
            WebRequest request) {
        
        log.error("Error interno del servidor", ex);
        
        var errorResponse = new ErrorResponseRestV1DTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                LocalDateTime.now().toString(),
                request.getDescription(false)
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}