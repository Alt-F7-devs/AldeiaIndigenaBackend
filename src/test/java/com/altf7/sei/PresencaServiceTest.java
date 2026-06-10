package com.altf7.sei.service;

import com.altf7.sei.dto.presenca.PresencaResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Presenca;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.exception.ConflictException;
import com.altf7.sei.exception.NotFoundException;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.PresencaRepository;
import com.altf7.sei.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PresencaService")
class PresencaServiceTest {

    @Mock private PresencaRepository presencaRepository;
    @Mock private AlunoRepository    alunoRepository;
    @Mock private SalaRepository     salaRepository;
    @Mock private AlunoService       alunoService;

    @InjectMocks
    private PresencaService presencaService;

    // ── fixtures ──────────────────────────────────────────────────────────────

    private Aluno aluno;
    private Sala  sala;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId_sala(10);
        sala.setNum_sa("5A");

        aluno = new Aluno();
        aluno.setId_aluno(1);
        aluno.setNome("Tupã Silva");
        aluno.setCgm("CGM001");
        aluno.setSala(sala);
    }

    // ── registrarPresenca ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("registrarPresenca()")
    class RegistrarPresenca {

        @Test
        @DisplayName("deve persistir presença quando todos os dados são válidos")
        void devePersistirPresenca() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(alunoService.validarAlunoNaSala(1, 10)).thenReturn(true);
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(false);
            when(salaRepository.findById(10)).thenReturn(Optional.of(sala));

            presencaService.registrarPresenca("CGM001", 10);

            ArgumentCaptor<Presenca> captor = ArgumentCaptor.forClass(Presenca.class);
            verify(presencaRepository).save(captor.capture());
            Presenca salva = captor.getValue();
            assertThat(salva.getAluno()).isEqualTo(aluno);
            assertThat(salva.getSala()).isEqualTo(sala);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando aluno não existe")
        void deveLancarExcecaoAlunoNaoEncontrado() {
            when(alunoRepository.findByCgm("CGM999")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.registrarPresenca("CGM999", 10))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);

            verify(presencaRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando aluno não pertence à sala")
        void deveLancarExcecaoAlunoForaDaSala() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(alunoService.validarAlunoNaSala(1, 10)).thenReturn(false);

            assertThatThrownBy(() -> presencaService.registrarPresenca("CGM001", 10))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);

            verify(presencaRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar ConflictException quando presença já foi registrada")
        void deveLancarExcecaoPresencaDuplicada() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(alunoService.validarAlunoNaSala(1, 10)).thenReturn(true);
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(true);

            assertThatThrownBy(() -> presencaService.registrarPresenca("CGM001", 10))
                    .isInstanceOf(ConflictException.PresencaJaRegistradaException.class);

            verify(presencaRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando sala não existe")
        void deveLancarExcecaoSalaNaoEncontrada() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(alunoService.validarAlunoNaSala(1, 10)).thenReturn(true);
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(false);
            when(salaRepository.findById(10)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.registrarPresenca("CGM001", 10))
                    .isInstanceOf(NotFoundException.SalaNotFoundException.class);

            verify(presencaRepository, never()).save(any());
        }
    }

    // ── calcularFrequencia ────────────────────────────────────────────────────

    @Nested
    @DisplayName("calcularFrequencia()")
    class CalcularFrequencia {

        @Test
        @DisplayName("deve retornar 100% e status REGULAR quando presença existe")
        void deveRetornarCemPorCentoComPresenca() {
            when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(true);

            PresencaResponseDTO dto = presencaService.calcularFrequencia(1);

            assertThat(dto.percentualFrequencia()).isEqualTo(100.0);
            assertThat(dto.status()).isEqualTo("REGULAR");
            assertThat(dto.nomeAluno()).isEqualTo("Tupã Silva");
            assertThat(dto.cgm()).isEqualTo("CGM001");
            assertThat(dto.numSa()).isEqualTo("5A");
        }

        @Test
        @DisplayName("deve retornar 0% e status REPROVADO quando presença não existe")
        void deveRetornarZeroPorCentoSemPresenca() {
            when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(false);

            PresencaResponseDTO dto = presencaService.calcularFrequencia(1);

            assertThat(dto.percentualFrequencia()).isEqualTo(0.0);
            assertThat(dto.status()).isEqualTo("REPROVADO");
        }

        @Test
        @DisplayName("deve retornar 'Sem sala' quando aluno não possui sala")
        void deveRetornarSemSalaQuandoAlunoSemSala() {
            aluno.setSala(null);
            when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));

            PresencaResponseDTO dto = presencaService.calcularFrequencia(1);

            assertThat(dto.numSa()).isEqualTo("Sem sala");
            assertThat(dto.percentualFrequencia()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando aluno não existe")
        void deveLancarExcecaoAlunoNaoEncontrado() {
            when(alunoRepository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.calcularFrequencia(99))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);
        }
    }

    // ── calcularFrequenciaTodos ───────────────────────────────────────────────

    @Nested
    @DisplayName("calcularFrequenciaTodos()")
    class CalcularFrequenciaTodos {

        @Test
        @DisplayName("deve calcular percentual corretamente para lista de alunos")
        void deveCalcularPercentualParaTodos() {
            Aluno aluno2 = new Aluno();
            aluno2.setId_aluno(2);
            aluno2.setNome("Iara Santos");
            aluno2.setCgm("CGM002");
            aluno2.setSala(null);

            when(alunoRepository.findAll()).thenReturn(List.of(aluno, aluno2));

            // aluno 1: 2 salas, 2 presenças → 100%
            when(alunoRepository.countSalasByAluno(1)).thenReturn(2L);
            when(presencaRepository.countByAluno(1)).thenReturn(2L);

            // aluno 2: 0 salas → 0%
            when(alunoRepository.countSalasByAluno(2)).thenReturn(0L);
            when(presencaRepository.countByAluno(2)).thenReturn(0L);

            List<PresencaResponseDTO> lista = presencaService.calcularFrequenciaTodos();

            assertThat(lista).hasSize(2);

            PresencaResponseDTO dto1 = lista.get(0);
            assertThat(dto1.percentualFrequencia()).isEqualTo(100.0);
            assertThat(dto1.status()).isEqualTo("REGULAR");

            PresencaResponseDTO dto2 = lista.get(1);
            assertThat(dto2.percentualFrequencia()).isEqualTo(0.0);
            assertThat(dto2.status()).isEqualTo("REPROVADO");
            assertThat(dto2.numSa()).isEqualTo("Sem sala");
        }

        @Test
        @DisplayName("deve retornar status ALERTA quando percentual está entre 75% e 79%")
        void deveRetornarStatusAlerta() {
            when(alunoRepository.findAll()).thenReturn(List.of(aluno));
            when(alunoRepository.countSalasByAluno(1)).thenReturn(4L);
            when(presencaRepository.countByAluno(1)).thenReturn(3L); // 75%

            List<PresencaResponseDTO> lista = presencaService.calcularFrequenciaTodos();

            assertThat(lista.get(0).percentualFrequencia()).isEqualTo(75.0);
            assertThat(lista.get(0).status()).isEqualTo("ALERTA");
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não há alunos")
        void deveRetornarListaVazia() {
            when(alunoRepository.findAll()).thenReturn(List.of());

            List<PresencaResponseDTO> lista = presencaService.calcularFrequenciaTodos();

            assertThat(lista).isEmpty();
        }
    }

    // ── removerPresenca ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("removerPresenca()")
    class RemoverPresenca {

        @Test
        @DisplayName("deve remover presença quando ela existe")
        void deveRemoverPresenca() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(true);

            presencaService.removerPresenca("CGM001", 10);

            verify(presencaRepository).deleteByAlunoESala(1, 10);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando aluno não existe")
        void deveLancarExcecaoAlunoNaoEncontrado() {
            when(alunoRepository.findByCgm("CGM999")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.removerPresenca("CGM999", 10))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);

            verify(presencaRepository, never()).deleteByAlunoESala(anyInt(), anyInt());
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando presença não existe")
        void deveLancarExcecaoPresencaNaoRegistrada() {
            when(alunoRepository.findByCgm("CGM001")).thenReturn(Optional.of(aluno));
            when(presencaRepository.existsByAlunoESala(1, 10)).thenReturn(false);

            assertThatThrownBy(() -> presencaService.removerPresenca("CGM001", 10))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);

            verify(presencaRepository, never()).deleteByAlunoESala(anyInt(), anyInt());
        }
    }
}