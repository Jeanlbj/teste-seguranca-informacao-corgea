package com.exercicio.extra.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String perfil
) {}
