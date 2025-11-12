package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
    List<Promocao> findByDataValidadeAfter(LocalDate hoje);
}