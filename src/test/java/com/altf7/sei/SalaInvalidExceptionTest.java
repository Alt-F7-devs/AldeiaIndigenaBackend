package com.altf7.sei.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaInvalidExceptionTest {

    @Test
    void deveRetornarMensagemSalaNaoEncontrada() {

        SalaInvalidException.SalaNotFoundExceptionAll ex =
                new SalaInvalidException.SalaNotFoundExceptionAll();

        assertEquals(
                "ERROR: Sala não foi encontrada!",
                ex.getMessage()
        );
    }

    @Test
    void deveRetornarMensagemSalaNaoEncontradaPorId() {

        SalaInvalidException.SalaNotFoundExceptionId ex =
                new SalaInvalidException.SalaNotFoundExceptionId(10);

        assertEquals(
                "ERROR: Sala não foi encontrada com o ID:10",
                ex.getMessage()
        );
    }
}