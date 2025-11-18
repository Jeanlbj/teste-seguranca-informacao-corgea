package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findByEsporteId(Long esporteId);
}
