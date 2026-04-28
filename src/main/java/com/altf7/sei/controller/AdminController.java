package com.altf7.sei.controller;

import com.altf7.sei.dto.admin.AdminRequestDTO;
import com.altf7.sei.entity.Admin;
import com.altf7.sei.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<Admin> criarAdmin(@RequestBody AdminRequestDTO req) {
        Admin adm = adminService.criarAdmin(req);
        return ResponseEntity.status(201).body(adm);
    }
}
