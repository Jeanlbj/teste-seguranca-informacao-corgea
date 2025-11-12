package com.exercicio.extra.dto;

import java.time.LocalDate;

public record PromocaoDTO(
        Long id,
        String titulo,
        String descricao,
        Double valorBonus,
        LocalDate dataValidade
) {}