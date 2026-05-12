package com.altf7.sei.service;

import com.altf7.sei.dto.aluno.AlunoRequestDTO;
import com.altf7.sei.dto.aluno.AlunoResponseDTO;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.repository.AlunoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public AlunoService(AlunoRepository alunoRepository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.alunoRepository = alunoRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Transactional
    public Aluno criarAluno(AlunoRequestDTO req) {
        if (req.cgm() == null || req.senha() == null || req.senha().isBlank()) {
            throw new RuntimeException("Login e senha não deve ser vazio!");
        } else if (alunoRepository.findByCgm(req.cgm()).isPresent()) {
            throw new RuntimeException("Login já cadastrado!");
        } else if (req.senha().length() < 6) {
            throw new RuntimeException("Senha não pode ter menos que 6 caracteres");
        } else if (!req.senha().matches(".*\\d.*")) {
            throw new RuntimeException("Senha deve conter pelo menos 1 número");
        } else if (!req.senha().matches(".*[a-zA-Z].*")) {
            throw new RuntimeException("Senha deve conter pelo menos 1 letra");
        } else if (!req.senha().matches(".*[@#$%!].*")) {
            throw new RuntimeException("Senha deve possuir no mínimo 1 caractere especial: !,@,#,$,%");
        }
        try {
            Aluno aluno = new Aluno();
            aluno.setNome(req.nome());
            aluno.setCgm(req.cgm());
            aluno.setSenha(passwordEncoder.encode(req.senha()));
            aluno.setAdmin_login(req.admin_login());
            return entityManager.merge(aluno);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar Aluno" + e.getMessage());
        }
    }

    public List<AlunoResponseDTO> listarAluno() {
        try {
            return alunoRepository.findAll()
                    .stream()
                    .map(aluno -> new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm()))
                    .toList();
        }catch (RuntimeException e) {
            throw new RuntimeException("Erro ao listar Alunos"+e.getMessage());
        }
    }

    public List<AlunoResponseDTO> listarAlunoPorId(Integer id_aluno) {
        try {
            return alunoRepository.findById(id_aluno)
                    .stream()
                    .map(aluno -> new AlunoResponseDTO(aluno.getId_aluno(), aluno.getNome(), aluno.getCgm()))
                    .toList();
        }catch (RuntimeException e) {
            throw new RuntimeException("Erro ao listar Alunos"+e.getMessage());
        }
    }

    @Transactional
    public AlunoResponseDTO editarAluno(Integer id_aluno, AlunoRequestDTO req){
        Aluno aluno = alunoRepository.findById(id_aluno)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado, tente novamente!"));

        if(req.nome() != null) aluno.setNome(req.nome());
        if(req.senha() != null) aluno.setSenha(passwordEncoder.encode(req.senha()));

        alunoRepository.save(aluno);

        return new AlunoResponseDTO(aluno.getId_aluno(),aluno.getNome(),aluno.getCgm());
    }

    @Transactional
    public void excluirAluno(Integer id_aluno){
        Aluno aluno = alunoRepository.findById(id_aluno)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado, tente novamente!"));
        alunoRepository.delete(aluno);
    }
}