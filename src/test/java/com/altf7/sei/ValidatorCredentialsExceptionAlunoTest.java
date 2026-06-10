package com.altf7.sei.validator;

import com.altf7.sei.entity.Aluno;
import com.altf7.sei.exception.CgmInvalidException;
import com.altf7.sei.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorCredentialsExceptionAlunoTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private ValidatorCredentialsExceptionAluno validator;

    private static final String CGM_VALIDO = "1234567890"; // 10 dígitos



    @Test
    @DisplayName("Deve validar CGM válido sem lançar exceção")
    void deveValidarCgmValido() {
        assertDoesNotThrow(() -> validator.validatorCgm(CGM_VALIDO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CGM for nulo")
    void deveLancarExcecaoQuandoCgmForNulo() {

        CgmInvalidException ex = assertThrows(
                CgmInvalidException.class,
                () -> validator.validatorCgm(null)
        );

        assertEquals(
                "ERROR: Campo não pode estar em branco!",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CGM tiver menos de 10 caracteres")
    void deveLancarExcecaoQuandoCgmMenorQue10() {

        CgmInvalidException ex = assertThrows(
                CgmInvalidException.class,
                () -> validator.validatorCgm("123456789")
        );

        assertEquals(
                "ERROR: CGM deve conter 10 caracteres",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CGM tiver mais de 10 caracteres")
    void deveLancarExcecaoQuandoCgmMaiorQue10() {

        CgmInvalidException ex = assertThrows(
                CgmInvalidException.class,
                () -> validator.validatorCgm("12345678901")
        );

        assertEquals(
                "ERROR: CGM deve conter 10 caracteres",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando CGM já estiver cadastrado")
    void deveLancarExcecaoCgmJaCadastrado() {
        Aluno alunoExistente = new Aluno();
        alunoExistente.setCgm(CGM_VALIDO);
        when(alunoRepository.findByCgm(CGM_VALIDO)).thenReturn(Optional.of(alunoExistente));

        assertThrows(CgmInvalidException.class, () -> validator.validatorCgm(CGM_VALIDO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CGM for string vazia")
    void deveLancarExcecaoCgmVazio() {
        assertThrows(CgmInvalidException.class, () -> validator.validatorCgm(""));
    }

    @Test
    @DisplayName("validatorNull deve lançar exceção para CGM nulo")
    void validatorNullDeveLancarExcecao() {
        assertThrows(CgmInvalidException.class, () -> validator.validatorNull(null));
    }

    @Test
    @DisplayName("validatorSizeCgm deve lançar exceção para CGM com tamanho inválido")
    void validatorSizeCgmDeveLancarExcecao() {
        assertThrows(CgmInvalidException.class, () -> validator.validatorSizeCgm("123"));
    }

    @Test
    @DisplayName("validatorExistsCgm deve lançar exceção para CGM já existente")
    void validatorExistsCgmDeveLancarExcecao() {
        Aluno aluno = new Aluno();
        aluno.setCgm(CGM_VALIDO);
        when(alunoRepository.findByCgm(CGM_VALIDO)).thenReturn(Optional.of(aluno));

        assertThrows(CgmInvalidException.class, () -> validator.validatorExistsCgm(CGM_VALIDO));
    }
}
