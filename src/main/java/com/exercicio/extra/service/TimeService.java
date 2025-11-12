package com.exercicio.extra.service;

import com.exercicio.extra.entity.Esporte;
import com.exercicio.extra.entity.Time;
import com.exercicio.extra.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;
    private final EsporteService esporteService;

    public List<Time> listarTodos() {
        return timeRepository.findAll();
    }

    public Optional<Time> buscarPorId(Long id) {
        return timeRepository.findById(id);
    }

    public Time salvar(Time time) {
        return timeRepository.save(time);
    }

    public void deletar(Long id) {
        timeRepository.deleteById(id);
    }
}
