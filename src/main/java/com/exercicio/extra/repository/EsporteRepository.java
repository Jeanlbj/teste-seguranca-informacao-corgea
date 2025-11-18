package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Esporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EsporteRepository extends JpaRepository<Esporte, Long> {
    Optional<Esporte> findByNome(String nome);
}
