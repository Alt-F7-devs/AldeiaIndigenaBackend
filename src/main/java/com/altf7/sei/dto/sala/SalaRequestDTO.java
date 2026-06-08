package com.altf7.sei.dto.sala;

public record SalaRequestDTO(
        Integer admin_id,
        Integer jogo_id_jogo,
        Integer professor_id_professor,
        String num_sa
) {}
