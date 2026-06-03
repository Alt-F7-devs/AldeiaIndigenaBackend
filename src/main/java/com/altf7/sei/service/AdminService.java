package com.altf7.sei.service;

import com.altf7.sei.dto.admin.AdminRequestDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.repository.AdminRepository;
import com.altf7.sei.validator.PasswordValidator;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final PasswordValidator passwordValidator;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        EntityManager entityManager,
                        PasswordValidator passwordValidator) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.passwordValidator = passwordValidator;
    }

    @Transactional
    public Admin criarAdmin(AdminRequestDTO req) {
        if (adminRepository.findByLogin(req.login()).isPresent()) {
            throw new RuntimeException("Login já cadastrado!");
        }

        passwordValidator.validatorPassword(req.senha());

        try {
            Admin adm = new Admin();
            adm.setLogin(req.login());
            adm.setSenha(passwordEncoder.encode(req.senha()));
            entityManager.merge(adm);
            return adm;
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar Admin: " + e.getMessage());
        }
    }
}
