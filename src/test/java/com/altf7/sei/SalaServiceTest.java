package com.altf7.sei;

import com.altf7.sei.dto.sala.SalaListResponseDTO;
import com.altf7.sei.dto.sala.SalaRequestDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.entity.Jogo;
import com.altf7.sei.exception.*;
import com.altf7.sei.repository.*;
import com.altf7.sei.service.AlunoService;
import com.altf7.sei.service.JogoService;
import com.altf7.sei.service.SalaService;
import com.altf7.sei.validator.ValidadorCredentialsExceptionSala;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private JogoRepository jogoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private ValidadorCredentialsExceptionSala validador;

    @Mock
    private JogoService jogoService;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private SalaService salaService;


    @Test
    @DisplayName("Deve criar sala com sucesso")
    void deveCriarSalaComSucesso() {

        Admin admin = new Admin();
        admin.setId_admin(1);

        Sala sala = new Sala();
        sala.setNum_sa("A1");
        sala.setAdmin(admin);

        SalaRequestDTO dto = new SalaRequestDTO(
                1,
                null,
                null,
                "A1"
        );

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(entityManager.merge(any(Sala.class)))
                .thenReturn(sala);

        Sala resultado = salaService.criarSala(dto);

        assertNotNull(resultado);

        verify(adminRepository).findById(1);
        verify(entityManager).merge(any(Sala.class));
    }


    @Test
    @DisplayName("Deve lançar exceção quando admin não existir")
    void deveLancarExcecaoQuandoAdminNaoExiste() {

        SalaRequestDTO dto =
                new SalaRequestDTO(
                        99,
                        null,
                        null,
                        null
                );

        when(adminRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                AdminInvalidException.AdminNotFoundExceptionAll.class,
                () -> salaService.criarSala(dto)
        );
    }


    @Test
    @DisplayName("Deve criar sala com professor")
    void deveCriarSalaComProfessor() {

        Admin admin = new Admin();
        admin.setId_admin(1);

        Professor professor = new Professor();
        professor.setId_professor(2);

        Sala sala = new Sala();
        sala.setAdmin(admin);
        sala.setProfessor(professor);

        SalaRequestDTO dto = new SalaRequestDTO(
                1,
                null,
                2,
                "A1"
        );

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(professorRepository.findById(2))
                .thenReturn(Optional.of(professor));

        when(entityManager.merge(any(Sala.class)))
                .thenReturn(sala);

        Sala resultado = salaService.criarSala(dto);

        assertEquals(2, resultado.getProfessor().getId_professor());
    }


    @Test
    @DisplayName("Deve criar sala com jogo")
    void deveCriarSalaComJogo() {

        Admin admin = new Admin();
        admin.setId_admin(1);

        Jogo jogo = new Jogo();
        jogo.setId(10);

        Sala sala = new Sala();
        sala.setAdmin(admin);
        sala.setJogo(jogo);

        SalaRequestDTO dto = new SalaRequestDTO(
                1,
                10,
                null,
                "A1"
        );

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(jogoRepository.findById(10))
                .thenReturn(Optional.of(jogo));

        when(entityManager.merge(any(Sala.class)))
                .thenReturn(sala);

        Sala resultado = salaService.criarSala(dto);

        assertEquals(10, resultado.getJogo().getId());
    }


    @Test
    @DisplayName("Deve adicionar aluno à sala com sucesso")
    void deveAdicionarAlunoSalaComSucesso() {

        Aluno aluno = new Aluno();
        aluno.setId_aluno(1);
        aluno.setCgm("1");

        Sala sala = new Sala();
        sala.setId_sala(1);

        when(alunoRepository.findByCgm(anyString()))
                .thenReturn(Optional.of(aluno));

        when(salaRepository.findById(anyInt()))
                .thenReturn(Optional.of(sala));

        when(alunoRepository.save(any(Aluno.class)))
                .thenReturn(aluno);

        Aluno resultado = salaService.addAlunoSala("1", 1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_aluno());

        verify(alunoRepository).findByCgm("1");
        verify(salaRepository).findById(1);
        verify(alunoRepository).save(any(Aluno.class));
    }


    @Test
    @DisplayName("Deve editar num_sa da sala")
    void deveEditarSala() {

        Sala sala = new Sala();
        sala.setId_sala(1);
        sala.setNum_sa("A1");

        SalaRequestDTO dto = new SalaRequestDTO(
                null,
                null,
                null,
                "B1"
        );

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        when(entityManager.merge(any(Sala.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sala resultado = salaService.editarSala(1, dto);

        assertEquals("B1", resultado.getNum_sa());

        verify(entityManager).merge(any(Sala.class));
    }


    @Test
    @DisplayName("Deve lançar exceção ao editar sala inexistente")
    void deveLancarExcecaoAoEditarSalaInexistente() {

        SalaRequestDTO dto = new SalaRequestDTO(
                null,
                null,
                null,
                "B1"
        );

        when(salaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                SalaInvalidException.SalaNotFoundExceptionAll.class,
                () -> salaService.editarSala(1, dto)
        );
    }


    @Test
    @DisplayName("Deve desvincular professor da sala")
    void deveDesvincularProfessorSala() {

        Professor professor = new Professor();

        Sala sala = new Sala();
        sala.setProfessor(professor);

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        when(salaRepository.save(any()))
                .thenReturn(sala);

        Sala resultado =
                salaService.desvincularProfessorSala(1);

        assertNull(resultado.getProfessor());

        verify(salaRepository).save(sala);
    }


    @Test
    @DisplayName("Deve lançar exceção quando professor não existir")
    void deveLancarExcecaoQuandoProfessorNaoExiste() {

        Admin admin = new Admin();
        admin.setId_admin(1);

        SalaRequestDTO dto = new SalaRequestDTO(
                1,
                null,
                99,
                "A1"
        );

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(professorRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ProfessorInvalidException.ProfessorNotFoundExceptionAll.class,
                () -> salaService.criarSala(dto)
        );
    }


    @Test
    @DisplayName("Deve lançar exceção quando jogo não existir")
    void deveLancarExcecaoQuandoJogoNaoExiste() {

        Admin admin = new Admin();
        admin.setId_admin(1);

        SalaRequestDTO dto = new SalaRequestDTO(
                1,
                99,
                null,
                "A1"
        );

        when(adminRepository.findById(1))
                .thenReturn(Optional.of(admin));

        when(jogoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                JogoInvalidException.JogoNotFoundExceptionAll.class,
                () -> salaService.criarSala(dto)
        );
    }


    @Test
    @DisplayName("Deve lançar exceção quando aluno não existir")
    void deveLancarExcecaoQuandoAlunoNaoExiste() {

        Mockito.lenient()  // ← Permite stubbings desnecessários
                .when(alunoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                AlunoInvalidException.AlunoNotFoundExceptionAll.class,
                () -> salaService.addAlunoSala("1", 1)
        );
    }


    @Test
    @DisplayName("Deve lançar exceção ao desvincular professor de sala inexistente")
    void deveLancarExcecaoAoDesvincularProfessorSalaInexistente() {

        when(salaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                SalaInvalidException.SalaNotFoundExceptionAll.class,
                () -> salaService.desvincularProfessorSala(1)
        );
    }


    @Test
    @DisplayName("Deve lançar exceção ao deletar sala inexistente")
    void deveLancarExcecaoAoDeletarSalaInexistente() {

        when(salaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                SalaInvalidException.SalaNotFoundExceptionAll.class,
                () -> salaService.deletarSala(1)
        );
    }


    @Test
    @DisplayName("Deve editar admin da sala")
    void deveEditarAdminSala() {

        Sala sala = new Sala();
        sala.setId_sala(1);

        Admin admin = new Admin();
        admin.setId_admin(10);

        SalaRequestDTO dto = new SalaRequestDTO(
                10,
                null,
                null,
                null
        );

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        when(adminRepository.findById(10))
                .thenReturn(Optional.of(admin));

        when(entityManager.merge(any(Sala.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Sala resultado = salaService.editarSala(1, dto);

        assertEquals(10, resultado.getAdmin().getId_admin());
    }


    @Test
    @DisplayName("Deve editar jogo da sala")
    void deveEditarJogoSala() {

        Sala sala = new Sala();
        sala.setId_sala(1);

        Jogo jogo = new Jogo();
        jogo.setId(5);

        SalaRequestDTO dto = new SalaRequestDTO(
                null,
                5,
                null,
                null
        );

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        when(jogoRepository.findById(5))
                .thenReturn(Optional.of(jogo));

        when(entityManager.merge(any(Sala.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Sala resultado = salaService.editarSala(1, dto);

        assertEquals(5, resultado.getJogo().getId());
    }


    @Test
    @DisplayName("Deve editar professor da sala")
    void deveEditarProfessorSala() {

        Sala sala = new Sala();
        sala.setId_sala(1);

        Professor professor = new Professor();
        professor.setId_professor(7);

        SalaRequestDTO dto = new SalaRequestDTO(
                null,
                null,
                7,
                null
        );

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        when(professorRepository.findById(7))
                .thenReturn(Optional.of(professor));

        when(entityManager.merge(any(Sala.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Sala resultado = salaService.editarSala(1, dto);

        assertEquals(7, resultado.getProfessor().getId_professor());
    }


    @Test
    @DisplayName("Deve listar salas")
    void deveListarSalas() {

        Professor professor = new Professor();
        professor.setId_professor(1);
        professor.setNome("João");

        Sala sala = new Sala();
        sala.setId_sala(1);
        sala.setNum_sa("A1");
        sala.setProfessor(professor);

        when(salaRepository.findAll())
                .thenReturn(List.of(sala));

        List<SalaListResponseDTO> resultado =
                salaService.listarSala();

        assertEquals(1, resultado.size());
        assertEquals("A1", resultado.get(0).num_sa());
    }


    @Test
    @DisplayName("Deve listar sala por id")
    void deveListarSalaPorId() {

        Sala sala = new Sala();
        sala.setId_sala(1);
        sala.setNum_sa("A1");

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        List<SalaListResponseDTO> resultado =
                salaService.listarSalaPorId(1);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).id_sala());
    }

    @Test
    @DisplayName("Deve deletar sala")
    void deveDeletarSala() {

        Sala sala = new Sala();
        sala.setId_sala(1);

        when(salaRepository.findById(1))
                .thenReturn(Optional.of(sala));

        salaService.deletarSala(1);

        verify(salaRepository).delete(sala);
    }
}