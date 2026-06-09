package com.altf7.sei.service;

import com.altf7.sei.dto.aluno.AlunoRequestDTO;
import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.exception.AlunoInvalidException;
import com.altf7.sei.exception.InternalServerError;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.validator.PasswordValidator;
import com.altf7.sei.validator.ValidatorCredentialsExceptionAluno;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final PasswordValidator passwordValidator;
    private final ValidatorCredentialsExceptionAluno validatorCredentialsExceptionAluno;

    /* Cria Aluno */
    @Transactional
    public Aluno criarAluno(AlunoRequestDTO req) {
        passwordValidator.validatorPassword(req.senha());
        validatorCredentialsExceptionAluno.validatorCgm(req.cgm());
        try {
            Aluno aluno = new Aluno();
            aluno.setNome(req.nome());
            aluno.setCgm(req.cgm());
            aluno.setSenha(passwordEncoder.encode(req.senha()));
            aluno.setAdmin_login(req.admin_login());
            return entityManager.merge(aluno);
        } catch (DataAccessException ex) {
            throw new InternalServerError.AlunoInternalServerError();
        }
    }

    /* Listagem Geral de Alunos cadastrados */
    public List<AlunoResponseDTO> listarAluno() {
        try {
            return alunoRepository.findAll()
                    .stream()
                    .map(aluno -> new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm()))
                    .toList();
        }catch (DataAccessException ex) {
            throw new InternalServerError.AlunoListInternalServerError();
        }
    }

    /* Listagem de Aluno por ID */
    public List<AlunoResponseDTO> listarAlunoPorId(Integer id_aluno) {
        Aluno aluno = alunoRepository.findById(id_aluno)
                .orElseThrow(() -> new AlunoInvalidException.AlunoNotFoundExceptionId(id_aluno));
        return List.of(new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm()));
    }

    public List<AlunoResponseDTO> listarAlunoPorSala(Integer id_sala) {
        try {
            return alunoRepository.findBySala_IdSala(id_sala)
                    .stream()
                    .map(aluno -> new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm()))
                    .toList();
        } catch (DataAccessException ex) {
            throw new InternalServerError.AlunoListInternalServerError();
        }
    }

    /* Edição de dados de cadastro de Aluno */
    @Transactional
    public AlunoResponseDTO editarAluno(Integer id_aluno, AlunoRequestDTO req){
        Aluno aluno = alunoRepository.findById(id_aluno)
                .orElseThrow(AlunoInvalidException.AlunoNotFoundExceptionAll::new);

        if(req.nome() != null) aluno.setNome(req.nome());
        if(req.senha() != null) aluno.setSenha(passwordEncoder.encode(req.senha()));

        alunoRepository.save(aluno);

        return new AlunoResponseDTO(aluno.getId_aluno(),aluno.getNome(),aluno.getCgm());
    }

    /* Deletar entidade Aluno */
    @Transactional
    public void excluirAluno(Integer id_aluno){
        Aluno aluno = alunoRepository.findById(id_aluno)
                .orElseThrow(AlunoInvalidException.AlunoNotFoundExceptionAll::new);
        alunoRepository.delete(aluno);
    }
}