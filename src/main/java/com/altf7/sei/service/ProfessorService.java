package com.altf7.sei.service;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.dto.professor.ProfessorResponseDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.repository.ProfessorRepository;
import com.altf7.sei.validator.PasswordValidator;
import com.altf7.sei.validator.ValidatorCredentialsExceptionProfessor;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final PasswordValidator passwordValidator;
    private final ValidatorCredentialsExceptionProfessor validatorCredentialsExceptionProfessor;


    public ProfessorService(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder, EntityManager entityManager, PasswordValidator passwordValidator, ValidatorCredentialsExceptionProfessor validatorCredentialsExceptionProfessor){
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.passwordValidator = passwordValidator;
        this.validatorCredentialsExceptionProfessor = validatorCredentialsExceptionProfessor;
    }

    @Transactional
    public Professor criarProfessor(ProfessorRequestDTO req) {
        passwordValidator.validatorPassword(req.senha());
        validatorCredentialsExceptionProfessor.validatorCpf(req.cpf());
        try {
            Professor prof = new Professor();
            prof.setNome(req.nome());
            prof.setCpf(req.cpf());
            prof.setSenha(passwordEncoder.encode(req.senha()));
            prof.setAdmin_login(req.admin_login());
            return entityManager.merge(prof);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar Professor" + e.getMessage());
        }
    }

    public List<ProfessorResponseDTO> listarProfessor() {
        try {
            return professorRepository.findAll()
                    .stream()
                    .map(professor -> new ProfessorResponseDTO(professor.getId_professor(), professor.getNome()))
                    .toList();
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao listar Professores" + e.getMessage());
        }
    }

    public List<ProfessorResponseDTO> listarProfessorPorId(Integer id_professor) {
        try {
            return professorRepository.findById(id_professor)
                    .stream()
                    .map(professor -> new ProfessorResponseDTO(professor.getId_professor(), professor.getNome()))
                    .toList();
        }catch (RuntimeException e) {
            throw new RuntimeException("Erro ao listar Professores"+e.getMessage());
        }
    }

    @Transactional
    public ProfessorResponseDTO editarProfessor(Integer id_professor, ProfessorRequestDTO req) {
        Professor prof = professorRepository.findById(id_professor)
                .orElseThrow(()-> new RuntimeException("Professor não encontrado, tente novamente!"));

        if(req.nome() != null) prof.setNome(req.nome());
        if(req.senha()!= null) prof.setSenha(passwordEncoder.encode(req.senha()));

        professorRepository.save(prof);

        return new ProfessorResponseDTO(prof.getId_professor(), prof.getNome());
    }

    public void excluirProfessor(Integer id_professor){
        Professor prof = professorRepository.findById(id_professor)
                .orElseThrow(()-> new RuntimeException("Professor não encontrado, tente novamente!"));
        professorRepository.delete(prof);
    }
}