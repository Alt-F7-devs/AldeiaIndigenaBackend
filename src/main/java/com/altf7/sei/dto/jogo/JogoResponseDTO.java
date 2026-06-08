package com.altf7.sei.dto.jogo;

import com.altf7.sei.entity.Jogo;


public record JogoResponseDTO(
        Integer id_jogo,
        String nome
) {
    public static JogoResponseDTO from(Jogo jogo) {
        return new JogoResponseDTO(jogo.getId(), jogo.getNome());
    }
}