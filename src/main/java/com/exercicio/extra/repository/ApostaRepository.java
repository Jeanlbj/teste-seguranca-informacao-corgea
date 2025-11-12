package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Aposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApostaRepository extends JpaRepository<Aposta, Long> {
    List<Aposta> findByUsuarioId(Long usuarioId);
    List<Aposta> findByEventoId(Long eventoId);
}
