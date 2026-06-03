package com.altf7.sei.service;

import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.exception.CredenciaisInvalidasException;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.ProfessorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthService authService;

    private Admin adminMock;
    private Professor professorMock;
    private Aluno alunoMock;

    @BeforeEach
    void setUp() {
        adminMock = new Admin();
        adminMock.setLogin("adminLogin");
        adminMock.setSenha("encodedAdminSenha");

        professorMock = new Professor();
        professorMock.setCpf("52998224725");
        professorMock.setSenha("encodedProfSenha");

        alunoMock = new Aluno();
        alunoMock.setCgm("1234567890");
        alunoMock.setSenha("encodedAlunoSenha");

        // Mockar sessão para evitar NPE no autenticar()
        lenient().when(request.getSession(true)).thenReturn(session);
        lenient().when(session.getId()).thenReturn("mock-session-id");
    }

    // ─── Login Professor ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Login como Admin deve retornar role ADMIN")
    void loginProfessorDeveRetornarRoleAdminQuandoAdminExistir() {
        when(adminRepository.findByLogin("adminLogin")).thenReturn(Optional.of(adminMock));
        when(passwordEncoder.matches("senha123", adminMock.getSenha())).thenReturn(true);

        LoginResponseDTO resultado = authService.loginProfessor("adminLogin", "senha123", request, response);

        assertEquals("ADMIN", resultado.tipo());
    }

    @Test
    @DisplayName("Login como Professor deve retornar role PROFESSOR")
    void loginProfessorDeveRetornarRoleProfessorQuandoCredenciaisCorretas() {
        when(adminRepository.findByLogin("52998224725")).thenReturn(Optional.empty());
        when(professorRepository.findByCpf("52998224725")).thenReturn(Optional.of(professorMock));
        when(passwordEncoder.matches("senha123", professorMock.getSenha())).thenReturn(true);

        LoginResponseDTO resultado = authService.loginProfessor("52998224725", "senha123", request, response);

        assertEquals("PROFESSOR", resultado.tipo());
    }

    @Test
    @DisplayName("Login professor deve lançar exceção quando credenciais forem inválidas")
    void loginProfessorDeveLancarExcecaoCredenciaisInvalidas() {
        when(adminRepository.findByLogin("cpf_invalido")).thenReturn(Optional.empty());
        when(professorRepository.findByCpf("cpf_invalido")).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class,
                () -> authService.loginProfessor("cpf_invalido", "senha", request, response));
    }

    @Test
    @DisplayName("Login professor deve lançar exceção quando senha do admin estiver errada")
    void loginProfessorDeveLancarExcecaoSenhaAdminErrada() {
        when(adminRepository.findByLogin("adminLogin")).thenReturn(Optional.of(adminMock));
        when(passwordEncoder.matches("senhaErrada", adminMock.getSenha())).thenReturn(false);
        when(professorRepository.findByCpf("adminLogin")).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class,
                () -> authService.loginProfessor("adminLogin", "senhaErrada", request, response));
    }

    @Test
    @DisplayName("Login professor deve lançar exceção quando senha do professor estiver errada")
    void loginProfessorDeveLancarExcecaoSenhaProfessorErrada() {
        when(adminRepository.findByLogin("52998224725")).thenReturn(Optional.empty());
        when(professorRepository.findByCpf("52998224725")).thenReturn(Optional.of(professorMock));
        when(passwordEncoder.matches("senhaErrada", professorMock.getSenha())).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class,
                () -> authService.loginProfessor("52998224725", "senhaErrada", request, response));
    }

    // ─── Login Aluno ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Login aluno deve retornar role ALUNO com credenciais corretas")
    void loginAlunoDeveRetornarRoleAluno() {
        when(alunoRepository.findByCgm("1234567890")).thenReturn(Optional.of(alunoMock));
        when(passwordEncoder.matches("senha123", alunoMock.getSenha())).thenReturn(true);

        LoginResponseDTO resultado = authService.loginAluno("1234567890", "senha123", request, response);

        assertEquals("ALUNO", resultado.tipo());
    }

    @Test
    @DisplayName("Login aluno deve lançar exceção quando CGM não for encontrado")
    void loginAlunoDeveLancarExcecaoCgmNaoEncontrado() {
        when(alunoRepository.findByCgm("cgm_invalido")).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class,
                () -> authService.loginAluno("cgm_invalido", "senha", request, response));
    }

    @Test
    @DisplayName("Login aluno deve lançar exceção quando senha estiver errada")
    void loginAlunoDeveLancarExcecaoSenhaErrada() {
        when(alunoRepository.findByCgm("1234567890")).thenReturn(Optional.of(alunoMock));
        when(passwordEncoder.matches("senhaErrada", alunoMock.getSenha())).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class,
                () -> authService.loginAluno("1234567890", "senhaErrada", request, response));
    }
}
