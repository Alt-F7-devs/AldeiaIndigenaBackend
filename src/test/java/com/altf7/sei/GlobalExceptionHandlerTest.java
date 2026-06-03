package com.altf7.sei.handler;

import com.altf7.sei.dto.error.ErrorResponse;
import com.altf7.sei.exception.CgmInvalidException;
import com.altf7.sei.exception.CpfInvalidException;
import com.altf7.sei.exception.PasswordInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve retornar 400 para CgmInvalidException")
    void deveRetornar400ParaCgmInvalidException() {
        CgmInvalidException ex = new CgmInvalidException("CGM inválido");

        ResponseEntity<ErrorResponse> response = handler.cgmInvalid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CGM inválido", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    @DisplayName("Deve retornar 400 para CpfInvalidException")
    void deveRetornar400ParaCpfInvalidException() {
        CpfInvalidException ex = new CpfInvalidException("CPF inválido");

        ResponseEntity<ErrorResponse> response = handler.cpfInvalid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CPF inválido", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    @DisplayName("Deve retornar 403 para PasswordInvalidException")
    void deveRetornar403ParaPasswordInvalidException() {
        PasswordInvalidException ex = new PasswordInvalidException("Senha inválida");

        ResponseEntity<ErrorResponse> response = handler.passwordInvalid(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Senha inválida", response.getBody().message());
        assertEquals(403, response.getBody().status());
    }

    @Test
    @DisplayName("Deve retornar 500 para Exception genérica")
    void deveRetornar500ParaExcecaoGenerica() {
        Exception ex = new Exception("Erro inesperado");

        ResponseEntity<ErrorResponse> response = handler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro interno do servidor", response.getBody().message());
        assertEquals(500, response.getBody().status());
    }
}
