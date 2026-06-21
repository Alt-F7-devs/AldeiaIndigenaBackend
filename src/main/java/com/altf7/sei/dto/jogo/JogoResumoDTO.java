package com.altf7.sei.dto.jogo;

import java.time.LocalDate;

public record JogoResumoDTO(
        Integer id_jogo,
        String nome,
        LocalDate data,
        long alunos,
        Integer id_sala
) {}