package com.altf7.sei.service;

import com.altf7.sei.entity.Professor;
import com.altf7.sei.exception.ProfessorInvalidException;
import com.altf7.sei.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthContextService {

    private final ProfessorRepository professorRepository;

    /* Retorna true se o usuário autenticado na sessão atual é ADMIN */
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /* Retorna true se o usuário autenticado na sessão atual é PROFESSOR */
    public boolean isProfessor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"));
    }

    /* Resolve o Professor autenticado na sessão atual a partir do CPF (principal) */
    public Professor getProfessorLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new ProfessorInvalidException.ProfessorNotFoundExceptionAll();
        }
        String cpf = auth.getPrincipal().toString();
        return professorRepository.findByCpf(cpf)
                .orElseThrow(ProfessorInvalidException.ProfessorNotFoundExceptionAll::new);
    }

    /* Retorna o id_professor do usuário autenticado (atalho) */
    public Integer getIdProfessorLogado() {
        return getProfessorLogado().getId_professor();
    }
}