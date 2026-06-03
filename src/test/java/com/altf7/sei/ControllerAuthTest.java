package com.altf7.sei.controller;

import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.dto.aluno.LoginAlunoRequestDTO;
import com.altf7.sei.dto.professor.LoginProfessorRequestDTO;
import com.altf7.sei.exception.CredenciaisInvalidasException;
import com.altf7.sei.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerAuthTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ControllerAuth controllerAuth;

    // ─── Login Professor ───────────────────────────────────────────────────────

    @Test
    @DisplayName("loginProfessor deve retornar 200 e role PROFESSOR com credenciais válidas")
    void loginProfessorDeveRetornar200RoleProfessor() {
        when(authService.loginProfessor(eq("52998224725"), eq("Senha1@"), any(), any()))
                .thenReturn(new LoginResponseDTO("PROFESSOR"));

        LoginProfessorRequestDTO dto = new LoginProfessorRequestDTO("52998224725", "Senha1@");
        ResponseEntity<LoginResponseDTO> resp = controllerAuth.loginProfessor(dto, request, response);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("PROFESSOR", resp.getBody().tipo());
    }

    @Test
    @DisplayName("loginProfessor deve retornar 200 e role ADMIN quando login for de admin")
    void loginProfessorDeveRetornar200RoleAdmin() {
        when(authService.loginProfessor(eq("adminLogin"), eq("Senha1@"), any(), any()))
                .thenReturn(new LoginResponseDTO("ADMIN"));

        LoginProfessorRequestDTO dto = new LoginProfessorRequestDTO("adminLogin", "Senha1@");
        ResponseEntity<LoginResponseDTO> resp = controllerAuth.loginProfessor(dto, request, response);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("ADMIN", resp.getBody().tipo());
    }

    @Test
    @DisplayName("loginProfessor deve propagar CredenciaisInvalidasException com credenciais erradas")
    void loginProfessorDeveLancarExcecaoCredenciaisInvalidas() {
        when(authService.loginProfessor(eq("cpf_errado"), eq("senha_errada"), any(), any()))
                .thenThrow(new CredenciaisInvalidasException("Credenciais inválidas"));

        LoginProfessorRequestDTO dto = new LoginProfessorRequestDTO("cpf_errado", "senha_errada");

        assertThrows(CredenciaisInvalidasException.class,
                () -> controllerAuth.loginProfessor(dto, request, response));
    }

    // ─── Login Aluno ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("loginAluno deve retornar 200 e role ALUNO com credenciais válidas")
    void loginAlunoDeveRetornar200RoleAluno() {
        when(authService.loginAluno(eq("1234567890"), eq("Senha1@"), any(), any()))
                .thenReturn(new LoginResponseDTO("ALUNO"));

        LoginAlunoRequestDTO dto = new LoginAlunoRequestDTO("1234567890", "Senha1@");
        ResponseEntity<LoginResponseDTO> resp = controllerAuth.loginAluno(dto, request, response);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("ALUNO", resp.getBody().tipo());
    }

    @Test
    @DisplayName("loginAluno deve propagar CredenciaisInvalidasException com CGM não encontrado")
    void loginAlunoDeveLancarExcecaoCgmNaoEncontrado() {
        when(authService.loginAluno(eq("cgm_errado"), eq("senha"), any(), any()))
                .thenThrow(new CredenciaisInvalidasException("Credenciais inválidas"));

        LoginAlunoRequestDTO dto = new LoginAlunoRequestDTO("cgm_errado", "senha");

        assertThrows(CredenciaisInvalidasException.class,
                () -> controllerAuth.loginAluno(dto, request, response));
    }

    @Test
    @DisplayName("loginAluno deve propagar CredenciaisInvalidasException com senha errada")
    void loginAlunoDeveLancarExcecaoSenhaErrada() {
        when(authService.loginAluno(eq("1234567890"), eq("senhaErrada"), any(), any()))
                .thenThrow(new CredenciaisInvalidasException("Credenciais inválidas"));

        LoginAlunoRequestDTO dto = new LoginAlunoRequestDTO("1234567890", "senhaErrada");

        assertThrows(CredenciaisInvalidasException.class,
                () -> controllerAuth.loginAluno(dto, request, response));
    }
}
