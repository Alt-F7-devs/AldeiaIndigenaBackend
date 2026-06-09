package com.altf7.sei.controller;

import com.altf7.sei.dto.aluno.AlunoRequestDTO;
import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/aluno")
public class AlunoController {

    private final AlunoService alunoService;

    // Cria Usuário Aluno
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody AlunoRequestDTO req) {
        Aluno aluno = alunoService.criarAluno(req);
        return ResponseEntity.status(201).body(
                new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm())
        );
    }

    // Executa a listagem de usuários Alunos
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAluno(){
        return ResponseEntity.ok(alunoService.listarAluno());
    }

    // Executa a listagem de usuário Aluno por Id
    @GetMapping("/{id_aluno}")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunoPorId(@PathVariable Integer id_aluno){
        return ResponseEntity.ok(alunoService.listarAlunoPorId(id_aluno));
    }

    // Executa uma atualização de dados de Usuário Aluno
    @PatchMapping("/{id_aluno}")
    public ResponseEntity<AlunoResponseDTO> editarAluno(@PathVariable Integer id_aluno, @RequestBody AlunoRequestDTO req) {
        return ResponseEntity.ok(alunoService.editarAluno(id_aluno, req));
    }

    // Deleta usuário Aluno
    @DeleteMapping("/{id_aluno}")
    public ResponseEntity<Void> excluirAluno(@PathVariable Integer id_aluno) {
        alunoService.excluirAluno(id_aluno);
        return ResponseEntity.noContent().build(); // retorna 204
    }
}