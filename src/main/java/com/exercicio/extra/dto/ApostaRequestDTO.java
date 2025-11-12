package com.exercicio.extra.dto;

public record ApostaRequestDTO(
        Long eventoId,
        Double valor,
        String escolha
) {}
