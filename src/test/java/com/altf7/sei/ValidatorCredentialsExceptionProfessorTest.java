package com.altf7.sei.validator;

import com.altf7.sei.entity.Professor;
import com.altf7.sei.exception.CpfInvalidException;
import com.altf7.sei.repository.ProfessorRepository;
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
class ValidatorCredentialsExceptionProfessorTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ValidatorCredentialsExceptionProfessor validator;

    private static final String CPF_VALIDO = "529.982.247-25";
    private static final String CPF_VALIDO_LIMPO = "52998224725";

    @Test
    @DisplayName("Deve validar CPF válido formatado com pontos e traço")
    void deveValidarCpfValidoFormatado() {
        assertDoesNotThrow(() -> validator.validatorCpf(CPF_VALIDO));
    }

    @Test
    @DisplayName("Deve validar CPF válido sem formatação")
    void deveValidarCpfValidoSemFormatacao() {
        assertDoesNotThrow(() -> validator.validatorCpf(CPF_VALIDO_LIMPO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for nulo")
    void deveLancarExcecaoQuandoCpfForNulo() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf(null)
        );
        assertEquals("ERROR: CPF não pode estar em branco", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for string vazia")
    void deveLancarExcecaoQuandoCpfForVazio() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf("")
        );
        assertEquals("ERROR: CPF não pode estar em branco", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for apenas espaços")
    void deveLancarExcecaoQuandoCpfForApenasEspacos() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf("   ")
        );
        assertEquals("ERROR: CPF não pode estar em branco", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF tiver menos de 11 dígitos")
    void deveLancarExcecaoCpfMenorQue11Digitos() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf("1234567890")
        );
        assertEquals("ERROR: CPF deve conter 11 dígitos", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF possuir todos os dígitos iguais")
    void deveLancarExcecaoCpfDigitosIguais() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf("11111111111")
        );
        assertEquals("ERROR: CPF inválido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF possuir dígito verificador inválido")
    void deveLancarExcecaoCpfDigitoVerificadorInvalido() {
        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf("12345678900")
        );
        assertEquals("ERROR: CPF inválido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já estiver cadastrado")
    void deveLancarExcecaoCpfJaCadastrado() {
        Professor professorExistente = new Professor();
        professorExistente.setCpf(CPF_VALIDO_LIMPO);
        when(professorRepository.findByCpf(CPF_VALIDO_LIMPO)).thenReturn(Optional.of(professorExistente));

        CpfInvalidException ex = assertThrows(
                CpfInvalidException.class,
                () -> validator.validatorCpf(CPF_VALIDO)
        );
        assertEquals("ERROR: CPF já cadastrado no sistema!", ex.getMessage());
    }
}
