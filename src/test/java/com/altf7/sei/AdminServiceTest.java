package com.altf7.sei.service;

import com.altf7.sei.dto.admin.AdminRequestDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.exception.PasswordInvalidException;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.validator.PasswordValidator;
import jakarta.persistence.EntityManager;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordValidator passwordValidator;

    @InjectMocks
    private AdminService adminService;

    private static final String LOGIN_VALIDO = "admin01";
    private static final String SENHA_VALIDA = "Senha1@";

    @BeforeEach
    void setUp() {
        lenient().when(adminRepository.findByLogin(LOGIN_VALIDO)).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("Deve criar admin com sucesso")
    void deveCriarAdminComSucesso() {
        when(passwordEncoder.encode(SENHA_VALIDA)).thenReturn("encodedSenha");

        Admin adminSalvo = new Admin();
        adminSalvo.setLogin(LOGIN_VALIDO);
        adminSalvo.setSenha("encodedSenha");
        when(entityManager.merge(any(Admin.class))).thenReturn(adminSalvo);

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, SENHA_VALIDA);
        Admin resultado = adminService.criarAdmin(req);

        assertNotNull(resultado);
        assertEquals(LOGIN_VALIDO, resultado.getLogin());
        verify(passwordValidator).validatorPassword(SENHA_VALIDA);
        verify(entityManager).merge(any(Admin.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando login já estiver cadastrado")
    void deveLancarExcecaoLoginJaCadastrado() {
        Admin adminExistente = new Admin();
        adminExistente.setLogin(LOGIN_VALIDO);
        when(adminRepository.findByLogin(LOGIN_VALIDO)).thenReturn(Optional.of(adminExistente));

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, SENHA_VALIDA);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adminService.criarAdmin(req));
        assertTrue(ex.getMessage().contains("Login já cadastrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando login já cadastrado não chama passwordValidator")
    void deveLancarExcecaoLoginJaCadastradoSemChamarValidator() {
        Admin adminExistente = new Admin();
        adminExistente.setLogin(LOGIN_VALIDO);
        when(adminRepository.findByLogin(LOGIN_VALIDO)).thenReturn(Optional.of(adminExistente));

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, SENHA_VALIDA);

        assertThrows(RuntimeException.class, () -> adminService.criarAdmin(req));
        verifyNoInteractions(passwordValidator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for inválida")
    void deveLancarExcecaoSenhaInvalida() {
        doThrow(new PasswordInvalidException("ERROR: Senha não pode conter menos que 6 caracteres"))
                .when(passwordValidator).validatorPassword("Ab1@");

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, "Ab1@");

        assertThrows(PasswordInvalidException.class, () -> adminService.criarAdmin(req));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver número")
    void deveLancarExcecaoSenhaSemNumero() {
        doThrow(new PasswordInvalidException("ERROR: Senha deve conter pelo menos 1 número"))
                .when(passwordValidator).validatorPassword("Senha@abc");

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, "Senha@abc");

        PasswordInvalidException ex = assertThrows(PasswordInvalidException.class,
                () -> adminService.criarAdmin(req));
        assertTrue(ex.getMessage().contains("pelo menos 1 número"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver letra")
    void deveLancarExcecaoSenhaSemLetra() {
        doThrow(new PasswordInvalidException("ERROR: Senha deve conter pelo menos 1 letra"))
                .when(passwordValidator).validatorPassword("12345@6");

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, "12345@6");

        PasswordInvalidException ex = assertThrows(PasswordInvalidException.class,
                () -> adminService.criarAdmin(req));
        assertTrue(ex.getMessage().contains("pelo menos 1 letra"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha não tiver caractere especial")
    void deveLancarExcecaoSenhaSemCaractereEspecial() {
        doThrow(new PasswordInvalidException("ERROR: Senha deve possuir no mínimo 1 caractere especial"))
                .when(passwordValidator).validatorPassword("Senha123");

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, "Senha123");

        PasswordInvalidException ex = assertThrows(PasswordInvalidException.class,
                () -> adminService.criarAdmin(req));
        assertTrue(ex.getMessage().contains("caractere especial"));
    }

    @Test
    @DisplayName("Deve codificar a senha antes de salvar")
    void deveCodificarSenhaAoSalvar() {
        when(passwordEncoder.encode(SENHA_VALIDA)).thenReturn("hashSenha");
        Admin adminRetornado = new Admin();
        adminRetornado.setSenha("hashSenha");
        when(entityManager.merge(any(Admin.class))).thenReturn(adminRetornado);

        AdminRequestDTO req = new AdminRequestDTO(LOGIN_VALIDO, SENHA_VALIDA);
        adminService.criarAdmin(req);

        verify(passwordEncoder).encode(SENHA_VALIDA);
    }
}
