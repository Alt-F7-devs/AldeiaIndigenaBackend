package com.altf7.sei.controller;

import com.altf7.sei.dto.LoginResponseDTO;
import com.altf7.sei.dto.aluno.LoginAlunoRequestDTO;
import com.altf7.sei.dto.professor.LoginProfessorRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.altf7.sei.service.AuthService;

@RestController
@RequestMapping("/auth")
public class ControllerAuth {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/professor")
    public ResponseEntity<LoginResponseDTO> loginprofessor(
            @RequestBody LoginProfessorRequestDTO dto) {
        return ResponseEntity.ok(
                authService.loginprofessor(dto.cpf(), dto.senha())
        );
    }

    @PostMapping("/login/aluno")
    public ResponseEntity<LoginResponseDTO> loginaluno(
            @RequestBody LoginAlunoRequestDTO dto){
        return ResponseEntity.ok(
                authService.loginaluno(dto.cgm(), dto.senha())
        );
    }
}
