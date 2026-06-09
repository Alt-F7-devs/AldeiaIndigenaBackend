package com.altf7.sei.validator;

import com.altf7.sei.exception.NumSaInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorCredentialsExceptionSalaTest {

    private ValidadorCredentialsExceptionSala validator;

    @BeforeEach
    void setUp() {
        validator = new ValidadorCredentialsExceptionSala();
    }

    @Test
    @DisplayName("Deve aceitar num_sa válido")
    void deveAceitarNumSaValido() {
        assertDoesNotThrow(() -> validator.validarNumSa("A1"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando num_sa for nulo")
    void deveLancarExcecaoQuandoNulo() {
        NumSaInvalidException ex = assertThrows(
                NumSaInvalidException.class,
                () -> validator.validarNumSa(null)
        );

        assertEquals(
                "ERROR: num_sa não pode estar em branco!",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando num_sa estiver vazio")
    void deveLancarExcecaoQuandoVazio() {
        assertThrows(
                NumSaInvalidException.class,
                () -> validator.validarNumSa("")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando possuir caracteres especiais")
    void deveLancarExcecaoCaracterEspecial() {
        NumSaInvalidException ex = assertThrows(
                NumSaInvalidException.class,
                () -> validator.validarNumSa("A@")
        );

        assertEquals(
                "ERROR: num_sa não pode conter caracteres especiais ou números negativos!",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando possuir mais de uma letra")
    void deveLancarExcecaoMaisDeUmaLetra() {
        NumSaInvalidException ex = assertThrows(
                NumSaInvalidException.class,
                () -> validator.validarNumSa("AB1")
        );

        assertEquals(
                "ERROR: num_sa deve conter no máximo 1 letra!",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando possuir mais de um número")
    void deveLancarExcecaoMaisDeUmNumero() {
        NumSaInvalidException ex = assertThrows(
                NumSaInvalidException.class,
                () -> validator.validarNumSa("A12")
        );

        assertEquals(
                "ERROR: num_sa deve conter no máximo 1 número!",
                ex.getMessage()
        );
    }
}