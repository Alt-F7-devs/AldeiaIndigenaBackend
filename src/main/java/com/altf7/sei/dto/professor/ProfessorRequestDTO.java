package com.altf7.sei.dto.professor;

public record ProfessorRequestDTO(
        String nome,
        String materia,
        String cpf,
        String senha
)
{}
