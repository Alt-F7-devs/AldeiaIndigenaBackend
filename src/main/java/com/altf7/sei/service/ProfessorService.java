package com.altf7.sei.service;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.dto.professor.ProfessorResponseDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.entity.Sala;
import com.altf7.sei.exception.InternalServerError;
import com.altf7.sei.exception.ProfessorInvalidException;
import com.altf7.sei.repository.ProfessorRepository;
import com.altf7.sei.repository.SalaRepository;
import com.altf7.sei.validator.PasswordValidator;
import com.altf7.sei.validator.ValidatorCredentialsExceptionProfessor;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final PasswordValidator passwordValidator;
    private final ValidatorCredentialsExceptionProfessor validatorCredentialsExceptionProfessor;
    private final SalaRepository salaRepository;

    /* Cria entidade professor */
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
        } catch (DataAccessException ex) {
            throw new InternalServerError.ProfessorInternalServerError();
        }
    }

    /* Listagem Geral de Professores cadastrados */
    public List<ProfessorResponseDTO> listarProfessor() {
        try {
            return professorRepository.findAll()
                    .stream()
                    .map(professor -> new ProfessorResponseDTO(professor.getId_professor(), professor.getNome()))
                    .toList();
        } catch (DataAccessException ex) {
            throw new InternalServerError.ProfessorListInternalServerError();
        }
    }

    /* Listagem de Professor por ID */
    public List<ProfessorResponseDTO> listarProfessorPorId(Integer id_professor) {
        Professor professor = professorRepository.findById(id_professor)
                .orElseThrow(() -> new ProfessorInvalidException.ProfessorNotFoundExceptionId(id_professor));
        return List.of(new ProfessorResponseDTO(professor.getId_professor(), professor.getNome()));
    }

    /* Edição de dados de cadastro de Professor */
    @Transactional
    public ProfessorResponseDTO editarProfessor(Integer id_professor, ProfessorRequestDTO req) {
        Professor prof = professorRepository.findById(id_professor)
                .orElseThrow(ProfessorInvalidException.ProfessorNotFoundExceptionAll::new);

        if(req.nome() != null) prof.setNome(req.nome());
        if(req.senha()!= null) prof.setSenha(passwordEncoder.encode(req.senha()));

        professorRepository.save(prof);

        return new ProfessorResponseDTO(prof.getId_professor(), prof.getNome());
    }

    /* Deletar entidade Professor */
    @Transactional
    public void excluirProfessor(Integer id_professor){
        Professor prof = professorRepository.findById(id_professor)
                .orElseThrow(ProfessorInvalidException.ProfessorNotFoundExceptionAll::new);

        List<Sala> salas = salaRepository.findByProfessor(prof);
        if (!salas.isEmpty()) {
            throw new ProfessorInvalidException.ProfessorComSalasVinculadasException();
        }

        professorRepository.delete(prof);
    }
}