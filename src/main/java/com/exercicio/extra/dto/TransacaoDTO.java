package com.exercicio.extra.dto;

import java.time.LocalDateTime;

public record TransacaoDTO(
        Long id,
        String tipo,
        Double valor,
        LocalDateTime data
) {}