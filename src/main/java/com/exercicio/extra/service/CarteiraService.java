package com.exercicio.extra.service;

import com.exercicio.extra.entity.Carteira;
import com.exercicio.extra.entity.TipoTransacao;
import com.exercicio.extra.repository.CarteiraRepository;
import com.exercicio.extra.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;
    private final TransacaoService transacaoService;

    public Optional<Carteira> buscarPorUsuarioId(Long usuarioId) {
        return carteiraRepository.findByUsuarioId(usuarioId);
    }

    public Carteira salvar(Carteira carteira) {
        return carteiraRepository.save(carteira);
    }

    @Transactional
    public void adicionarSaldo(Carteira carteira, Double valor) {
        carteira.setSaldo(carteira.getSaldo() + valor);
        carteiraRepository.save(carteira);

        transacaoService.criarTransacao(carteira, valor, TipoTransacao.DEPOSITO);
    }
}