package com.altf7.sei.controller;

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

    @PostMapping("/{cgm}/sala/{idSala}")
    public ResponseEntity<Void> registrarPresenca(
            @PathVariable String cgm,
            @PathVariable Integer idSala) {
        presencaService.registrarPresenca(cgm, idSala);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @DeleteMapping("/{cgm}/sala/{idSala}")
    public ResponseEntity<Void> removerPresenca(
            @PathVariable Integer idSala,
            @PathVariable String cgm) {
        presencaService.removerPresenca(cgm, idSala);
        return ResponseEntity.noContent().build();
    }
}