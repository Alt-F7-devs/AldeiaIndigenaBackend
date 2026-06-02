package com.altf7.sei.controller;

import com.altf7.sei.dto.jogo.JogoRequestDTO;
import com.altf7.sei.dto.jogo.JogoResponseDTO;
import com.altf7.sei.service.JogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jogos")
@RequiredArgsConstructor
public class JogoController {
    private final JogoService jogoService;

    @GetMapping
    public ResponseEntity<List<JogoResponseDTO>> listar() {
        return ResponseEntity.ok(jogoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(jogoService.buscarporid(id));
    }

    @PostMapping
    public ResponseEntity<JogoResponseDTO> criar(@RequestBody JogoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> atualizar(@PathVariable Integer id, @RequestBody JogoRequestDTO request) {
        return ResponseEntity.ok(jogoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        jogoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
