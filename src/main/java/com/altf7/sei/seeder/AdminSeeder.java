package com.altf7.sei.seeder;

import com.altf7.sei.domain.Admin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.altf7.sei.domain.Admin;
import com.altf7.sei.repository.AdminRepository;

@Component
public class AdminSeeder implements ApplicationRunner {

    @Value("${app.admin.password}")
    private String adminPassword;

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder;

    public AdminSeeder(AdminRepository adminRepository, BCryptPasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setLogin(1);
            admin.setSenha(encoder.encode(adminPassword));
            adminRepository.save(admin);
        }
    }
}
