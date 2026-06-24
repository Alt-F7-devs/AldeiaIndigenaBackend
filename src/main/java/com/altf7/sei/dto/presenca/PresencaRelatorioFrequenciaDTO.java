package com.altf7.sei.dto.presenca;

public record PresencaRelatorioFrequenciaDTO (
        String nome,
        String cgm,
        Integer idSala,
        String numSala,
        long presencas,
        long totalJogos,
        double percentual,
        String status
)
{}
