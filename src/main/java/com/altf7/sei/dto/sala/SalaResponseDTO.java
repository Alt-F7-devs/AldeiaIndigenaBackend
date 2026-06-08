package com.altf7.sei.dto.sala;

import java.time.LocalDate;

public record SalaResponseDTO(
        Integer id_sala,
        String numSa,
        LocalDate data,

        String professorNome,
        String jogoNome
)

{}