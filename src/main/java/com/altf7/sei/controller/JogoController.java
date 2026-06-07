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

    /* Listar jogos já cadastrados (GERAL) */
    @GetMapping
    public ResponseEntity<List<JogoResponseDTO>> listar() {
        return ResponseEntity.ok(jogoService.listar());
    }

    /* Listar jogos já cadastrados (ID) */
    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(jogoService.buscarPorId(id));
    }

    /* Cria Jogo */
    @PostMapping
    public ResponseEntity<JogoResponseDTO> criarJogo(@RequestBody JogoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoService.criarJogo(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> atualizarJogo(@PathVariable Integer id, @RequestBody JogoRequestDTO request) {
        return ResponseEntity.ok(jogoService.atualizarJogo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJogo(@PathVariable Integer id) {
        jogoService.deletarJogo(id);
        return ResponseEntity.noContent().build();
    }
}
