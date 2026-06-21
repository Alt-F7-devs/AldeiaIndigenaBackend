package com.altf7.sei.dto.sala;

import java.time.LocalDate;
import java.util.List;

public record SalaListResponseDTO(
        Integer id_sala,
        String num_sa,
        LocalDate data,
        Integer id_professor,
        String nome,
        List<String> jogosNomes
)
{}