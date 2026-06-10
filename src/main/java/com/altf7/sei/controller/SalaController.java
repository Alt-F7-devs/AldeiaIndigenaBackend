package com.altf7.sei.controller;

import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.dto.jogo.JogoResponseDTO;
import com.altf7.sei.dto.jogo.SalaJogoDTO;
import com.altf7.sei.dto.sala.AlunoSalaDTO;
import com.altf7.sei.dto.sala.SalaListResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.service.JogoService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.altf7.sei.service.SalaService;
import com.altf7.sei.dto.sala.SalaRequestDTO;
import com.altf7.sei.dto.sala.SalaResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sala")
public class SalaController {

    private final SalaService salaService;
    private final JogoService jogoService;

    /* Endpoint --> Criação de Sala */
    @PostMapping
    public ResponseEntity<SalaResponseDTO> criarSala(@RequestBody SalaRequestDTO req) {
        Sala sala = salaService.criarSala(req);
        String professorNome = sala.getProfessor() != null ? sala.getProfessor().getNome() : null;
        String jogoNome = sala.getJogo() != null ? sala.getJogo().getNome() : null;
        return ResponseEntity.status(201).body(
                new SalaResponseDTO(sala.getId_sala(), sala.getNum_sa(), sala.getData(), professorNome, jogoNome)
        );
    }

    /* Endpoint --> Adicionar Aluno a uma Sala já cadastrada */
    @PostMapping("/{id_sala}/aluno/{cgm}")
    public ResponseEntity<AlunoSalaDTO> addAlunoSala(@PathVariable("id_sala") Integer id_sala, @PathVariable("cgm") String cgm) {
        Aluno aluno = salaService.addAlunoSala(cgm, id_sala);
        AlunoSalaDTO dto = new AlunoSalaDTO(
                aluno.getId_aluno(),
                aluno.getNome(),
                aluno.getCgm(),
                aluno.getSala().getId_sala(),
                aluno.getSala().getNum_sa()
        );

        return ResponseEntity.status(201).body(dto);
    }

    // Executa a listagem de usuário Aluno
    @GetMapping("/{id_sala}/alunos")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunosDaSala(@PathVariable Integer id_sala) {
        return ResponseEntity.ok(salaService.listarAlunoSala(id_sala));
    }

    // Executa a listagem de usuário Aluno por Id
    @GetMapping("/aluno/{id_aluno}")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunoPorIdSala(@PathVariable Integer id_aluno){
        return ResponseEntity.ok(salaService.listarAlunoPorIdSala(id_aluno));
    }

    /* Listar salas já cadastradas (GERAL) */
    @GetMapping
    public ResponseEntity<List<SalaListResponseDTO>> listarSala(){
        return ResponseEntity.ok(salaService.listarSala());
    }

    /* Listar salas já cadastradas (ID) */
    @GetMapping("/{id_sala}")
    public ResponseEntity<List<SalaListResponseDTO>> listarSalaPorId(@PathVariable Integer id_sala){
        return ResponseEntity.ok(salaService.listarSalaPorId(id_sala));
    }

    /* Endpoint --> Edição parcial de Sala */
    @PatchMapping("/{id_sala}")
    public ResponseEntity<SalaResponseDTO> editarSala(@PathVariable Integer id_sala, @RequestBody SalaRequestDTO req) {
        Sala sala = salaService.editarSala(id_sala, req);
        String professorNome = sala.getProfessor() != null ? sala.getProfessor().getNome() : null;
        String jogoNome = sala.getJogo() != null ? sala.getJogo().getNome() : null;
        return ResponseEntity.ok(
                new SalaResponseDTO(sala.getId_sala(), sala.getNum_sa(), sala.getData(), professorNome, jogoNome)
        );
    }

    /* Endpoint --> Desvincular Professor de uma Sala (set null) */
    @DeleteMapping("/{id_sala}/professor")
    public ResponseEntity<SalaResponseDTO> desvincularProfessorSala(@PathVariable Integer id_sala) {
        Sala sala = salaService.desvincularProfessorSala(id_sala);
        String professorNome = sala.getProfessor() != null ? sala.getProfessor().getNome() : null;
        String jogoNome = sala.getJogo() != null ? sala.getJogo().getNome() : null;
        return ResponseEntity.ok(
                new SalaResponseDTO(sala.getId_sala(), sala.getNum_sa(), sala.getData(), professorNome, jogoNome)
        );
    }

    /* Deleta Sala */
    @DeleteMapping("/{id_sala}")
    public ResponseEntity<Void> deletarSala(@PathVariable Integer id_sala) {
        salaService.deletarSala(id_sala);
        return ResponseEntity.noContent().build();
    }

    /* Endpoint --> Adicionar Jogo a uma Sala já cadastrada */
    @PostMapping("/{id_sala}/jogos/{id_jogo}")
    public ResponseEntity<SalaJogoDTO> addJogoSala(@PathVariable Integer id_sala, @PathVariable Integer id_jogo) {
        Sala sala = salaService.addJogoSala(id_sala, id_jogo);
        SalaJogoDTO dto = new SalaJogoDTO(
                sala.getId_sala(),
                sala.getNum_sa(),
                sala.getJogo().getId(),
                sala.getJogo().getNome()
        );
        return ResponseEntity.status(201).body(dto);
    }

    /* Listar jogos já cadastrados (GERAL) */
    @GetMapping("/jogos")
    public ResponseEntity<List<JogoResponseDTO>> listarJogoSala() {
        return ResponseEntity.ok(salaService.listarJogoSala());
    }

    /* Listar jogos já cadastrados (ID) */
    @GetMapping("/jogos/{id}")
    public ResponseEntity<JogoResponseDTO> buscarJogoPorIdSala(@PathVariable Integer id) {
        return ResponseEntity.ok(salaService.buscarJogoPorIdSala(id));
    }
}