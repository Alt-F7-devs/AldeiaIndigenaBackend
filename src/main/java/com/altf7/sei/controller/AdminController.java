package com.altf7.sei.controller;

import com.altf7.sei.dto.admin.AdminRequestDTO;
import com.altf7.sei.dto.admin.AdminResponseDTO;
import com.altf7.sei.dto.aluno.AlunoRequestDTO;
import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.dto.professor.ProfessorResponseDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.service.AdminService;
import com.altf7.sei.service.AlunoService;
import com.altf7.sei.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {


    private final AdminService adminService;
    private final AlunoService alunoService;
    private final ProfessorService professorService;

    // Cria Usuário Admin
    @PostMapping
    public ResponseEntity<AdminResponseDTO> criarAdmin(@RequestBody AdminRequestDTO req) {
        Admin adm = adminService.criarAdmin(req);
        return ResponseEntity.status(201).body(
                new AdminResponseDTO(adm.getId_admin(), adm.getLogin())
        );
    }

    // Cria Usuário Aluno
    @PostMapping("/aluno")
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody AlunoRequestDTO req) {
        Aluno aluno = alunoService.criarAluno(req);
        return ResponseEntity.status(201).body(
                new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm())
        );
    }

    // Executa a listagem de usuários Alunos
    @GetMapping("/aluno")
    public ResponseEntity<List<AlunoResponseDTO>> listarAluno(){
        return ResponseEntity.ok(alunoService.listarAluno());
    }

    // Executa a listagem de usuário Aluno por Id
    @GetMapping("/aluno/{id_aluno}")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunoPorId(@PathVariable Integer id_aluno){
        return ResponseEntity.ok(alunoService.listarAlunoPorId(id_aluno));
    }

    // Executa uma atualização de dados de Usuário Aluno
    @PatchMapping("/aluno/{id_aluno}")
    public ResponseEntity<AlunoResponseDTO> editarAluno(@PathVariable Integer id_aluno, @RequestBody AlunoRequestDTO req) {
        return ResponseEntity.ok(alunoService.editarAluno(id_aluno, req));
    }

    // Deleta usuário Aluno
    @DeleteMapping("/aluno/{id_aluno}")
    public ResponseEntity<Void> excluirAluno(@PathVariable Integer id_aluno) {
        alunoService.excluirAluno(id_aluno);
        return ResponseEntity.noContent().build(); // retorna 204
    }

    // Cria Usuário Professor
    @PostMapping("/professor")
    public ResponseEntity<ProfessorResponseDTO> criarProfessor(@RequestBody ProfessorRequestDTO req) {
        Professor prof = professorService.criarProfessor(req);
        return ResponseEntity.status(201).body(
                new ProfessorResponseDTO(prof.getId_professor(), prof.getNome(), prof.getCpf())
        );
    }

    //  Executa a listagem de usuários Professor
    @GetMapping("/professor")
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessor(){
        return ResponseEntity.ok(professorService.listarProfessor());
    }

    //  Executa a listagem de usuários Professor por Id
    @GetMapping("/professor/{id_professor}")
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessorId(@PathVariable Integer id_professor) {
        return ResponseEntity.ok(professorService.listarProfessorPorId(id_professor));
    }

    // Executa uma atualização de dados de Usuário Professor
    @PatchMapping("/professor/{id_professor}")
    public ResponseEntity<ProfessorResponseDTO> editarProfessor(@PathVariable Integer id_professor, @RequestBody ProfessorRequestDTO req) {
        return ResponseEntity.ok(professorService.editarProfessor(id_professor,req));
    }

    // Deleta Usuário Professor
    @DeleteMapping("/professor/{id_professor}")
    public ResponseEntity<Void> excluirProfessor(@PathVariable Integer id_professor) {
        professorService.excluirProfessor(id_professor);
        return ResponseEntity.noContent().build();
    }
}