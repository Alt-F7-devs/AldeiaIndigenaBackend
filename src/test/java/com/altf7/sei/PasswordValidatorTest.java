package com.altf7.sei.validator;

import com.altf7.sei.exception.PasswordInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    @DisplayName("Deve aceitar senha válida com letra, número e caractere especial")
    void deveAceitarSenhaValida() {
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1@"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for vazia")
    void deveLancarExcecaoQuandoSenhaForVazia() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("")
        );
        assertEquals("ERROR: Senha não pode conter menos que 6 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for nula")
    void deveLancarExcecaoQuandoSenhaForNula() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword(null)
        );
        assertEquals("ERROR: Senha não pode conter menos que 6 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for apenas espaços em branco")
    void deveLancarExcecaoQuandoSenhaForApenasEspacos() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("      ")
        );
        assertEquals("ERROR: Senha não pode conter menos que 6 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tiver menos de 6 caracteres")
    void deveLancarExcecaoSenhaMenorQue6Caracteres() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("Ab1@")
        );
        assertEquals("ERROR: Senha não pode conter menos que 6 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tiver exatamente 5 caracteres")
    void deveLancarExcecaoSenhaComExatamente5Caracteres() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("Ab1@x")
        );
        assertEquals("ERROR: Senha não pode conter menos que 6 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar senha com exatamente 6 caracteres válidos")
    void deveAceitarSenhaComExatamente6Caracteres() {
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("aB1@cd"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver número")
    void deveLancarExcecaoSenhaSemNumero() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("Senha@abc")
        );
        assertEquals("ERROR: Senha deve conter pelo menos 1 número", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver caractere especial")
    void deveLancarExcecaoSenhaSemCaractereEspecial() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("Senha123")
        );
        assertEquals("ERROR: Senha deve possuir no mínimo 1 caractere especial", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver letra")
    void deveLancarExcecaoSenhaSemLetra() {
        PasswordInvalidException ex = assertThrows(
                PasswordInvalidException.class,
                () -> passwordValidator.validatorPassword("12345@6")
        );
        assertEquals("ERROR: Senha deve conter pelo menos 1 letra", ex.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar todos os caracteres especiais permitidos")
    void deveAceitarTodosCaracteresEspeciais() {
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1!"));
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1@"));
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1#"));
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1$"));
        assertDoesNotThrow(() -> passwordValidator.validatorPassword("Senha1%"));
    }
}
