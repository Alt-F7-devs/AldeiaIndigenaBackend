package com.altf7.sei.controller;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.dto.professor.ProfessorResponseDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    // Cria Usuário Professor
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criarProfessor(@RequestBody ProfessorRequestDTO req) {
        Professor prof = professorService.criarProfessor(req);
        return ResponseEntity.status(201).body(
                new ProfessorResponseDTO(prof.getId_professor(), prof.getNome())
        );
    }

    // Executa a listagem de usuários Professor
    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessor(){
        return ResponseEntity.ok(professorService.listarProfessor());
    }

    // Executa a listagem de usuários Professor por Id
    @GetMapping("/{id_professor}")
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessorId(@PathVariable Integer id_professor) {
        return ResponseEntity.ok(professorService.listarProfessorPorId(id_professor));
    }

    // Executa uma atualização de dados de Usuário Professor
    @PatchMapping("/{id_professor}")
    public ResponseEntity<ProfessorResponseDTO> editarProfessor(@PathVariable Integer id_professor, @RequestBody ProfessorRequestDTO req) {
        return ResponseEntity.ok(professorService.editarProfessor(id_professor, req));
    }

    // Deleta Usuário Professor
    @DeleteMapping("/{id_professor}")
    public ResponseEntity<Void> excluirProfessor(@PathVariable Integer id_professor) {
        professorService.excluirProfessor(id_professor);
        return ResponseEntity.noContent().build();
    }
}