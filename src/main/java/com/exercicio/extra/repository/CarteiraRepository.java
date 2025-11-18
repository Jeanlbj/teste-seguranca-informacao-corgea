package com.exercicio.extra.repository;

import com.exercicio.extra.entity.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    Optional<Carteira> findByUsuarioId(Long usuarioId);
}
