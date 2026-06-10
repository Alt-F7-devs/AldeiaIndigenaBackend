package com.altf7.sei.controller;

import com.altf7.sei.dto.presenca.PresencaResponseDTO;
import com.altf7.sei.exception.ConflictException;
import com.altf7.sei.exception.NotFoundException;
import com.altf7.sei.service.PresencaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PresencaController")
class PresencaControllerTest {

    @Mock  private PresencaService presencaService;
    @InjectMocks private PresencaController presencaController;

    // ── fixture ───────────────────────────────────────────────────────────────

    private PresencaResponseDTO buildDTO(double percentual, String status) {
        return new PresencaResponseDTO("Tupã Silva", "CGM001", "5A", percentual, status);
    }

    // ── POST /{cgm}/sala/{idSala} ─────────────────────────────────────────────

    @Nested
    @DisplayName("POST /{cgm}/sala/{idSala}")
    class RegistrarPresenca {

        @Test
        @DisplayName("deve retornar 201 CREATED ao registrar presença com sucesso")
        void deveRetornar201() {
            doNothing().when(presencaService).registrarPresenca("CGM001", 10);

            ResponseEntity<Void> response = presencaController.registrarPresenca("CGM001", 10);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            verify(presencaService).registrarPresenca("CGM001", 10);
        }

        @Test
        @DisplayName("deve propagar NotFoundException quando aluno não existe")
        void devePropagarnNotFoundException() {
            doThrow(NotFoundException.AlunoNotFoundException.class)
                    .when(presencaService).registrarPresenca("CGM999", 10);

            assertThatThrownBy(() -> presencaController.registrarPresenca("CGM999", 10))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);
        }

        @Test
        @DisplayName("deve propagar ConflictException quando presença já existe")
        void devePropagarConflictException() {
            doThrow(ConflictException.PresencaJaRegistradaException.class)
                    .when(presencaService).registrarPresenca("CGM001", 10);

            assertThatThrownBy(() -> presencaController.registrarPresenca("CGM001", 10))
                    .isInstanceOf(ConflictException.PresencaJaRegistradaException.class);
        }
    }

    // ── GET /frequencia/{idAluno} ─────────────────────────────────────────────

    @Nested
    @DisplayName("GET /frequencia/{idAluno}")
    class CalcularFrequencia {

        @Test
        @DisplayName("deve retornar 200 com DTO quando aluno existe")
        void deveRetornar200ComDTO() {
            PresencaResponseDTO dto = buildDTO(100.0, "REGULAR");
            when(presencaService.calcularFrequencia(1)).thenReturn(dto);

            ResponseEntity<PresencaResponseDTO> response =
                    presencaController.calcularFrequencia(1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(dto);
        }

        @Test
        @DisplayName("deve propagar NotFoundException quando aluno não existe")
        void devePropagarnNotFoundException() {
            when(presencaService.calcularFrequencia(99))
                    .thenThrow(NotFoundException.AlunoNotFoundException.class);

            assertThatThrownBy(() -> presencaController.calcularFrequencia(99))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);
        }
    }

    // ── GET /frequencia ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /frequencia")
    class CalcularFrequenciaTodos {

        @Test
        @DisplayName("deve retornar 200 com lista de DTOs")
        void deveRetornar200ComLista() {
            List<PresencaResponseDTO> lista = List.of(
                    buildDTO(100.0, "REGULAR"),
                    buildDTO(0.0,   "REPROVADO")
            );
            when(presencaService.calcularFrequenciaTodos()).thenReturn(lista);

            ResponseEntity<List<PresencaResponseDTO>> response =
                    presencaController.calcularFrequenciaTodos();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não há alunos")
        void deveRetornar200ComListaVazia() {
            when(presencaService.calcularFrequenciaTodos()).thenReturn(List.of());

            ResponseEntity<List<PresencaResponseDTO>> response =
                    presencaController.calcularFrequenciaTodos();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    // ── DELETE /{cgm}/sala/{idSala} ───────────────────────────────────────────

    @Nested
    @DisplayName("DELETE /{cgm}/sala/{idSala}")
    class RemoverPresenca {

        @Test
        @DisplayName("deve retornar 204 NO_CONTENT ao remover com sucesso")
        void deveRetornar204() {
            doNothing().when(presencaService).removerPresenca("CGM001", 10);

            ResponseEntity<Void> response = presencaController.removerPresenca(10, "CGM001");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(presencaService).removerPresenca("CGM001", 10);
        }

        @Test
        @DisplayName("deve propagar NotFoundException quando aluno ou presença não existe")
        void devePropagarnNotFoundException() {
            doThrow(NotFoundException.AlunoNotFoundException.class)
                    .when(presencaService).removerPresenca("CGM999", 10);

            assertThatThrownBy(() -> presencaController.removerPresenca(10, "CGM999"))
                    .isInstanceOf(NotFoundException.AlunoNotFoundException.class);
        }
    }
}