package com.altf7.sei.service;

import com.altf7.sei.dto.admin.AdminRequestDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.repository.AdminRepository;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
    public class AdminService {

        private final AdminRepository adminRepository;
        private final PasswordEncoder passwordEncoder;
        private final EntityManager entityManager;

        public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, EntityManager entityManager){
            this.adminRepository = adminRepository;
            this.passwordEncoder = passwordEncoder;
            this.entityManager = entityManager;
        }

        @Transactional
        public Admin criarAdmin(AdminRequestDTO req) {
            if (req.login() == null || req.senha().isBlank()) {
                throw new RuntimeException("Login e senha não deve ser vazio!");
            } else if (adminRepository.findByLogin(req.login()).isPresent()) {
                throw new RuntimeException("Login já cadastrado!");
            }
            else if (req.senha().length() < 6) {
                throw new RuntimeException("Senha não pode ter menos que 6 caracteres");
            } else if(!req.senha().matches(".*\\d.*")){
                throw new RuntimeException("Senha deve conter pelo menos 1 número");
            } else if(!req.senha().matches(".*[a-zA-Z].*")) {
                throw new RuntimeException("Senha deve conter pelo menos 1 letra");
            } else if(!req.senha().matches(".*[@#$%!].*")) {
                throw new RuntimeException("Senha deve possuir no mínimo 1 caractere especial: !,@,#,$,%");
            }
            try {
                Admin adm = new Admin();
                adm.setLogin(req.login());
                adm.setSenha(passwordEncoder.encode(req.senha()));
                entityManager.merge(adm);
                return adm;
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar Admin:"+e.getMessage());
            }
        }
    }