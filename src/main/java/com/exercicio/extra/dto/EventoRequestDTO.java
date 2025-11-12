package com.exercicio.extra.dto;

import java.time.LocalDateTime;

public record EventoRequestDTO(
        Long esporteId,
        Long timeAId,
        Long timeBId,
        LocalDateTime dataHora,
        String local,
        Double oddTimeA,
        Double oddTimeB,
        Double oddEmpate
) {}