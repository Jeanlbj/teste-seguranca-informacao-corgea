package com.exercicio.extra.service;

import com.exercicio.extra.entity.Evento;
import com.exercicio.extra.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public List<Evento> listarProximosEventos() {
        return eventoRepository.findByDataHoraAfter(LocalDateTime.now());
    }

    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public void deletar(Long id) {
        eventoRepository.deleteById(id);
    }
}
