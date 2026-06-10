package com.altf7.sei.dto.presenca;

public record PresencaResponseDTO(
        String nomeAluno,
        String cgm,
        String numSa,
        double percentualFrequencia,
        String status
)
{}
