package com.altf7.sei.service;

import com.altf7.sei.dto.aluno.AlunoRequestDTO;
import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.exception.AlunoInvalidException;
import com.altf7.sei.exception.PasswordInvalidException;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.validator.PasswordValidator;
import com.altf7.sei.validator.ValidatorCredentialsExceptionAluno;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private ValidatorCredentialsExceptionAluno validatorCredentialsExceptionAluno;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno alunoMock;

    @BeforeEach
    void setUp() {
        alunoMock = new Aluno();
        alunoMock.setId_aluno(1);
        alunoMock.setNome("João da Silva");
        alunoMock.setCgm("1234567890");
        alunoMock.setSenha("encodedSenha");
        alunoMock.setAdmin_login(1);
    }

    @Test
    @DisplayName("Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() {
        when(passwordEncoder.encode(any())).thenReturn("encodedSenha");
        when(entityManager.merge(any(Aluno.class))).thenReturn(alunoMock);

        AlunoRequestDTO req = new AlunoRequestDTO("João da Silva", "Senha1@", "1234567890", 1);
        Aluno resultado = alunoService.criarAluno(req);

        assertNotNull(resultado);
        assertEquals("João da Silva", resultado.getNome());
        verify(passwordValidator).validatorPassword("Senha1@");
        verify(validatorCredentialsExceptionAluno).validatorCgm("1234567890");
        verify(entityManager).merge(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando validação de senha falhar ao criar aluno")
    void deveLancarExcecaoValidacaoSenhaFalhar() {
        doThrow(new PasswordInvalidException("Senha inválida"))
                .when(passwordValidator).validatorPassword(any());

        AlunoRequestDTO req = new AlunoRequestDTO("João", "senhafraca", "1234567890", 1);

        assertThrows(PasswordInvalidException.class, () -> alunoService.criarAluno(req));
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosAlunos() {
        Aluno aluno2 = new Aluno();
        aluno2.setId_aluno(2);
        aluno2.setNome("Maria");
        aluno2.setCgm("0987654321");

        when(alunoRepository.findAll()).thenReturn(List.of(alunoMock, aluno2));

        List<AlunoResponseDTO> resultado = alunoService.listarAluno();

        assertEquals(2, resultado.size());
        assertEquals("João da Silva", resultado.get(0).nome());
        assertEquals("Maria", resultado.get(1).nome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver alunos")
    void deveRetornarListaVaziaQuandoNaoHouverAlunos() {
        when(alunoRepository.findAll()).thenReturn(List.of());

        List<AlunoResponseDTO> resultado = alunoService.listarAluno();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve listar aluno por ID existente")
    void deveListarAlunoPorIdExistente() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(alunoMock));

        List<AlunoResponseDTO> resultado = alunoService.listarAlunoPorId(1);

        assertEquals(1, resultado.size());
        assertEquals("João da Silva", resultado.get(0).nome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando aluno não for encontrado pelo ID")
    void deveLancarExcecaoQuandoIdNaoForEncontrado() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        AlunoInvalidException.AlunoNotFoundExceptionId ex =
                assertThrows(
                        AlunoInvalidException.AlunoNotFoundExceptionId.class,
                        () -> alunoService.listarAlunoPorId(99)
                );

        assertEquals(
                "ERROR: Aluno não foi encontrado com o ID:99",
                ex.getMessage()
        );
    }

    @Test
    @DisplayName("Deve editar nome do aluno com sucesso")
    void deveEditarNomeAlunoComSucesso() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(alunoMock));
        when(alunoRepository.save(alunoMock)).thenReturn(alunoMock);

        AlunoRequestDTO req = new AlunoRequestDTO("Novo Nome", null, null, 1);
        AlunoResponseDTO resultado = alunoService.editarAluno(1, req);

        assertEquals("Novo Nome", resultado.nome());
        verify(alunoRepository).save(alunoMock);
    }

    @Test
    @DisplayName("Deve editar senha do aluno com sucesso")
    void deveEditarSenhaAlunoComSucesso() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(alunoMock));
        when(passwordEncoder.encode("NovaSenha1@")).thenReturn("novaSenhaEncoded");
        when(alunoRepository.save(alunoMock)).thenReturn(alunoMock);

        AlunoRequestDTO req = new AlunoRequestDTO(null, "NovaSenha1@", null, 1);
        alunoService.editarAluno(1, req);

        verify(passwordEncoder).encode("NovaSenha1@");
        verify(alunoRepository).save(alunoMock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar aluno inexistente")
    void deveLancarExcecaoAoEditarAlunoInexistente() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        AlunoRequestDTO req = new AlunoRequestDTO("Nome", null, null, 1);

        assertThrows(RuntimeException.class, () -> alunoService.editarAluno(99, req));
    }

    @Test
    @DisplayName("Deve excluir aluno com sucesso")
    void deveExcluirAlunoComSucesso() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(alunoMock));

        assertDoesNotThrow(() -> alunoService.excluirAluno(1));
        verify(alunoRepository).delete(alunoMock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir aluno inexistente")
    void deveLancarExcecaoAoExcluirAlunoInexistente() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> alunoService.excluirAluno(99));
        assertTrue(ex.getMessage().contains("ERROR: Aluno não foi encontrado!"));
    }
}
