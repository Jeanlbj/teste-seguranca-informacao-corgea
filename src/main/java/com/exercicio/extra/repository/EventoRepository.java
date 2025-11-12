package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByDataHoraAfter(LocalDateTime dataHora);
}
