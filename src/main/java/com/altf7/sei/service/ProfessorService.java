package com.altf7.sei.service;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    public ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }

    public Professor criarProfessor(ProfessorRequestDTO req){

        Professor prof = new Professor();
        prof.setNome(req.nome());
        prof.setCpf(req.cpf());
        prof.setSenha(req.senha());

        return professorRepository.save(prof);
    }


}
