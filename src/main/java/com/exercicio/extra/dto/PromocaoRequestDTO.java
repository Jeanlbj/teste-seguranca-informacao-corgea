package com.exercicio.extra.dto;

import java.time.LocalDate;

public record PromocaoRequestDTO(
        String titulo,
        String descricao,
        Double valorBonus,
        LocalDate dataValidade
) {}