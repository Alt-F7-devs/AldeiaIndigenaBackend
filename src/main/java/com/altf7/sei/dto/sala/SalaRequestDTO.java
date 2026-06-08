package com.altf7.sei.dto.sala;

import java.time.LocalDate;

public record SalaRequestDTO(
        String numSa,
        LocalDate data
)

{}
