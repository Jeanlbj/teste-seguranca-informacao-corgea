package com.exercicio.extra.service;

import com.exercicio.extra.entity.Promocao;
import com.exercicio.extra.repository.PromocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;

    public Promocao salvar(Promocao promocao) {
        return promocaoRepository.save(promocao);
    }

    public List<Promocao> listarPromocoesValidas() {
        return promocaoRepository.findByDataValidadeAfter(LocalDate.now().minusDays(1));
    }

    public Optional<Promocao> buscarPorId(Long id) {
        return promocaoRepository.findById(id);
    }

    public void deletar(Long id) {
        promocaoRepository.deleteById(id);
    }
}