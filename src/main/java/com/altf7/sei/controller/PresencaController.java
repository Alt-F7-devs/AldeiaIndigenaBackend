package com.altf7.sei.controller;

import com.altf7.sei.dto.presenca.PresencaDetalheDTO;
import com.altf7.sei.dto.presenca.PresencaRelatorioFrequenciaDTO;
import com.altf7.sei.dto.presenca.PresencaResponseDTO;
import com.altf7.sei.service.PresencaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/presencas")
@RequiredArgsConstructor
public class PresencaController {

    private final PresencaService presencaService;

    @PostMapping("/{cgm}/jogo/{idJogo}")
    public ResponseEntity<Void> registrarPresenca(
            @PathVariable String cgm,
            @PathVariable Integer idJogo) {
        presencaService.registrarPresenca(cgm, idJogo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/jogo/{idJogo}")
    public ResponseEntity<List<Integer>> listarPresencasDoJogo(@PathVariable Integer idJogo) {
        return ResponseEntity.ok(presencaService.listarPresencasDoJogo(idJogo));
    }

    @GetMapping("/frequencia/{idAluno}")
    public ResponseEntity<PresencaResponseDTO> calcularFrequencia(
            @PathVariable Integer idAluno) {
        return ResponseEntity.ok(presencaService.calcularFrequencia(idAluno));
    }

    @GetMapping("/frequencia")
    public ResponseEntity<List<PresencaResponseDTO>> calcularFrequenciaTodos() {
        return ResponseEntity.ok(presencaService.calcularFrequenciaTodos());
    }

    @DeleteMapping("/{cgm}/jogo/{idJogo}")
    public ResponseEntity<Void> removerPresenca(
            @PathVariable Integer idJogo,
            @PathVariable String cgm) {
        presencaService.removerPresenca(cgm, idJogo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/frequencia/relatorio")
    public ResponseEntity<List<PresencaRelatorioFrequenciaDTO>> gerarRelatorioFrequencia() {
        return ResponseEntity.ok(presencaService.gerarRelatorioFrequencia());
    }

    @GetMapping("/aluno/{cgm}")
    public ResponseEntity<List<PresencaDetalheDTO>> listarPresencasDoAluno(@PathVariable String cgm) {
        return ResponseEntity.ok(presencaService.listarPresencasDoAluno(cgm));
    }
}