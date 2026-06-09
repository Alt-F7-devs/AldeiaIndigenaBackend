package com.altf7.sei.service;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.dto.professor.ProfessorResponseDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.exception.PasswordInvalidException;
import com.altf7.sei.exception.ProfessorInvalidException;
import com.altf7.sei.repository.ProfessorRepository;
import com.altf7.sei.repository.SalaRepository;
import com.altf7.sei.validator.PasswordValidator;
import com.altf7.sei.validator.ValidatorCredentialsExceptionProfessor;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private ValidatorCredentialsExceptionProfessor validatorCredentialsExceptionProfessor;

    @InjectMocks
    private ProfessorService professorService;

    private Professor professorMock;

    @Mock
    private SalaRepository salaRepository;

    @BeforeEach
    void setUp() {
        professorMock = new Professor();
        professorMock.setId_professor(1);
        professorMock.setNome("Ana Souza");
        professorMock.setCpf("52998224725");
        professorMock.setSenha("encodedSenha");
        professorMock.setAdmin_login(1);
    }

    @Test
    @DisplayName("Deve criar professor com sucesso")
    void deveCriarProfessorComSucesso() {
        when(passwordEncoder.encode(any())).thenReturn("encodedSenha");
        when(entityManager.merge(any(Professor.class))).thenReturn(professorMock);

        ProfessorRequestDTO req = new ProfessorRequestDTO("Ana Souza", "52998224725", "Senha1@", 1);
        Professor resultado = professorService.criarProfessor(req);

        assertNotNull(resultado);
        assertEquals("Ana Souza", resultado.getNome());
        verify(passwordValidator).validatorPassword("Senha1@");
        verify(validatorCredentialsExceptionProfessor).validatorCpf("52998224725");
        verify(entityManager).merge(any(Professor.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando validação de senha falhar ao criar professor")
    void deveLancarExcecaoValidacaoSenhaFalhar() {
        doThrow(new PasswordInvalidException("Senha inválida"))
                .when(passwordValidator).validatorPassword(any());

        ProfessorRequestDTO req = new ProfessorRequestDTO("Ana", "52998224725", "fraco", 1);

        assertThrows(PasswordInvalidException.class, () -> professorService.criarProfessor(req));
    }

    @Test
    @DisplayName("Deve listar todos os professores")
    void deveListarTodosProfessores() {
        Professor prof2 = new Professor();
        prof2.setId_professor(2);
        prof2.setNome("Carlos Lima");

        when(professorRepository.findAll()).thenReturn(List.of(professorMock, prof2));

        List<ProfessorResponseDTO> resultado = professorService.listarProfessor();

        assertEquals(2, resultado.size());
        assertEquals("Ana Souza", resultado.get(0).nome());
        assertEquals("Carlos Lima", resultado.get(1).nome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver professores")
    void deveRetornarListaVaziaQuandoNaoHouverProfessores() {
        when(professorRepository.findAll()).thenReturn(List.of());

        List<ProfessorResponseDTO> resultado = professorService.listarProfessor();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve listar professor por ID existente")
    void deveListarProfessorPorIdExistente() {
        when(professorRepository.findById(1)).thenReturn(Optional.of(professorMock));

        List<ProfessorResponseDTO> resultado = professorService.listarProfessorPorId(1);

        assertEquals(1, resultado.size());
        assertEquals("Ana Souza", resultado.get(0).nome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando professor não for encontrado pelo ID")
    void deveLancarExcecaoQuandoProfessorNaoExistir() {
        when(professorRepository.findById(99)).thenReturn(Optional.empty());

        ProfessorInvalidException.ProfessorNotFoundExceptionId ex =
                assertThrows(
                        ProfessorInvalidException.ProfessorNotFoundExceptionId.class,
                        () -> professorService.listarProfessorPorId(99)
                );

        assertEquals(
                "ERROR: Professor não foi encontrado com o ID:99",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve editar nome do professor com sucesso")
    void deveEditarNomeProfessorComSucesso() {
        when(professorRepository.findById(1)).thenReturn(Optional.of(professorMock));
        when(professorRepository.save(professorMock)).thenReturn(professorMock);

        ProfessorRequestDTO req = new ProfessorRequestDTO("Novo Nome", null, null, 1);
        ProfessorResponseDTO resultado = professorService.editarProfessor(1, req);

        assertEquals("Novo Nome", resultado.nome());
        verify(professorRepository).save(professorMock);
    }

    @Test
    @DisplayName("Deve editar senha do professor com sucesso")
    void deveEditarSenhaProfessorComSucesso() {
        when(professorRepository.findById(1)).thenReturn(Optional.of(professorMock));
        when(passwordEncoder.encode("NovaSenha1@")).thenReturn("novaSenhaEncoded");
        when(professorRepository.save(professorMock)).thenReturn(professorMock);

        ProfessorRequestDTO req = new ProfessorRequestDTO(null, null, "NovaSenha1@", 1);
        professorService.editarProfessor(1, req);

        verify(passwordEncoder).encode("NovaSenha1@");
        verify(professorRepository).save(professorMock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar professor inexistente")
    void deveLancarExcecaoAoEditarProfessorInexistente() {
        when(professorRepository.findById(99)).thenReturn(Optional.empty());

        ProfessorRequestDTO req = new ProfessorRequestDTO("Nome", null, null, 1);

        assertThrows(RuntimeException.class, () -> professorService.editarProfessor(99, req));
    }

    @Test
    @DisplayName("Deve excluir professor com sucesso")
    void deveExcluirProfessorComSucesso() {
        when(professorRepository.findById(1))
                .thenReturn(Optional.of(professorMock));

        when(salaRepository.findByProfessor(professorMock))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> professorService.excluirProfessor(1));

        verify(professorRepository).delete(professorMock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir professor inexistente")
    void deveLancarExcecaoAoExcluirProfessorInexistente() {
        ProfessorInvalidException.ProfessorNotFoundExceptionAll ex =
                assertThrows(
                        ProfessorInvalidException.ProfessorNotFoundExceptionAll.class,
                        () -> professorService.excluirProfessor(99)
                );

        assertEquals(
                "ERROR: Professor não foi encontrado!",
                ex.getMessage()
        );
    }
}
