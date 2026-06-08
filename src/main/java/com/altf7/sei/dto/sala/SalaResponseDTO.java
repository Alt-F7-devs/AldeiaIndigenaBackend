package com.altf7.sei.dto.sala;

import java.time.LocalDate;

public record SalaResponseDTO(
        Integer id_sala,
        String num_sa,
        LocalDate data,

        String nome,
        String jogoNome
)

{}