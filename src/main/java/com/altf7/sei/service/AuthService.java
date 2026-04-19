package com.altf7.sei.service;

import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public LoginResponseDTO loginprofessor(String cpf, String senha){
        Professor prof = professorRepository.findByCpf(cpf)
            .orElseThrow(() -> new RuntimeException("Credenciais Invalidas"));
        boolean senhaValida = passwordEncoder.matches(senha, prof.getSenha());
        if (!senhaValida){
            throw new RuntimeException("Credenciais Invalidas");
        }
        boolean isAdmin = adminRepository
                .findByLogin(Integer.valueOf(prof.getCpf()))
                .isPresent();
        String tipo = isAdmin ? "ADMIN" : "PROFESSOR";
        return new LoginResponseDTO(tipo);
    }

    public LoginResponseDTO loginaluno(Integer cgm, String senha){
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
