package com.exercicio.extra.service;

import com.exercicio.extra.entity.Esporte;
import com.exercicio.extra.repository.EsporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EsporteService {

    private final EsporteRepository esporteRepository;

    public List<Esporte> listarTodos() {
        return esporteRepository.findAll();
    }

    public Optional<Esporte> buscarPorId(Long id) {
        return esporteRepository.findById(id);
    }

    public Esporte salvar(Esporte esporte) {
        return esporteRepository.save(esporte);
    }

    public void deletar(Long id) {
        esporteRepository.deleteById(id);
    }
}
