package com.altf7.sei.service;

import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO loginProfessor(String cpf, String senha) {
        try {
            Optional<Admin> adminOpt = adminRepository.findByLogin(cpf);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (passwordEncoder.matches(senha, admin.getSenha())) {
                    return new LoginResponseDTO("ADMIN");
                }
            }
        } catch (NumberFormatException e) {
        }
        Optional<Professor> profOpt = professorRepository.findByCpf(cpf);
        if (profOpt.isPresent()) {
            Professor prof = profOpt.get();
            if (passwordEncoder.matches(senha, prof.getSenha())) {
                return new LoginResponseDTO("PROFESSOR");
            }
        }
        throw new RuntimeException("Credenciais inválidas");
    }

    public LoginResponseDTO loginAluno(String cgm, String senha){
        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(() -> new RuntimeException("Credenciais Invalidas"));
        boolean senhaValida = passwordEncoder.matches(senha, aluno.getSenha());
        if(!senhaValida){
            throw new RuntimeException("Credenciais Invalidas");
        }
        String tipo = "ALUNO";
        return new LoginResponseDTO(tipo);
    }
}