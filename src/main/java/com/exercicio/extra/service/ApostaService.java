package com.exercicio.extra.service;

import com.exercicio.extra.entity.Aposta;
import com.exercicio.extra.repository.ApostaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApostaService {

    private final ApostaRepository apostaRepository;

    public Aposta salvar(Aposta aposta) {
        return apostaRepository.save(aposta);
    }

    public List<Aposta> listarPorUsuarioId(Long usuarioId) {
        return apostaRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Aposta> buscarPorId(Long id) {
        return apostaRepository.findById(id);
    }

    public List<Aposta> listarPorEventoId(Long eventoId) {
        return apostaRepository.findByEventoId(eventoId);
    }
}
