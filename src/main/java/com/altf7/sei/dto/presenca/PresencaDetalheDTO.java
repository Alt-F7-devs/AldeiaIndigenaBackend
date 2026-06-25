package com.altf7.sei.dto.presenca;

import java.time.LocalDate;

public record PresencaDetalheDTO(
        Integer idJogo,
        String nomeJogo,
        LocalDate dataJogo
) {}