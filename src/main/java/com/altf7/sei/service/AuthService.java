package com.altf7.sei.service;

import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.entity.Aluno;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.repository.AlunoRepository;
import com.altf7.sei.repository.ProfessorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import com.altf7.sei.exception.CredenciaisInvalidasException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO loginProfessor(String cpf, String senha,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        // tenta login como Admin primeiro
        Optional<Admin> adminOpt = adminRepository.findByLogin(cpf);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(senha, admin.getSenha())) {
                autenticar("ROLE_ADMIN", cpf, request, response);
                return new LoginResponseDTO("ADMIN");
            }
        }

        // tenta login como Professor
        Optional<Professor> profOpt = professorRepository.findByCpf(cpf);
        if (profOpt.isPresent()) {
            Professor prof = profOpt.get();
            if (passwordEncoder.matches(senha, prof.getSenha())) {
                autenticar("ROLE_PROFESSOR", cpf, request, response);
                return new LoginResponseDTO("PROFESSOR");
            }
        }

        throw new CredenciaisInvalidasException("Credenciais inválidas");
    }

    public LoginResponseDTO loginAluno(String cgm, String senha,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        Aluno aluno = alunoRepository.findByCgm(cgm)
                .orElseThrow(() -> new CredenciaisInvalidasException("Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, aluno.getSenha())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas");
        }

        autenticar("ROLE_ALUNO", cgm, request, response);
        return new LoginResponseDTO("ALUNO");
    }

    private void autenticar(String role, String principal,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal, null, List.of(new SimpleGrantedAuthority(role))
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // salva na sessão HTTP para requisições futuras
        new HttpSessionSecurityContextRepository()
                .saveContext(context, request, response);
    }
}