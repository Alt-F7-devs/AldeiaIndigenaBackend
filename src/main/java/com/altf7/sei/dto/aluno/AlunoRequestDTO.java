package com.altf7.sei.dto.aluno;

public record AlunoRequestDTO(
        String nome,
        String senha,
        String cgm,
        Integer admin_login
)
{}
