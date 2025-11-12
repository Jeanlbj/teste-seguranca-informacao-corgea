package com.exercicio.extra.service;

import com.exercicio.extra.entity.Carteira;
import com.exercicio.extra.entity.TipoTransacao;
import com.exercicio.extra.entity.Transacao;
import com.exercicio.extra.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public void criarTransacao(Carteira carteira, Double valor, TipoTransacao tipo) {
        Transacao transacao = Transacao.builder()
                .carteira(carteira)
                .valor(valor)
                .tipo(tipo)
                .data(LocalDateTime.now())
                .build();
        transacaoRepository.save(transacao);
    }

    public List<Transacao> listarPorCarteira(Long carteiraId) {
        return transacaoRepository.findByCarteiraIdOrderByDataDesc(carteiraId);
    }
}