package com.altf7.sei.service;

import com.altf7.sei.dto.professor.ProfessorRequestDTO;
import com.altf7.sei.entity.Professor;
import com.altf7.sei.repository.ProfessorRepository;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public ProfessorService(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder, EntityManager entityManager){
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }


}
