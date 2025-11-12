package com.exercicio.extra.controller;

import com.exercicio.extra.dto.ApostaRequestDTO;
import com.exercicio.extra.entity.*;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.ApostaService;
import com.exercicio.extra.service.CarteiraService;
import com.exercicio.extra.service.EventoService;
import com.exercicio.extra.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/apostas")
@RequiredArgsConstructor
public class ApostaController {

    private final ApostaService apostaService;
    private final EventoService eventoService;
    private final CarteiraService carteiraService;
    private final TransacaoService transacaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> fazerAposta(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody ApostaRequestDTO dto) {
        var eventoOpt = eventoService.buscarPorId(dto.eventoId());
        if (eventoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Evento não encontrado");
        }

        Long usuarioId = userDetails.getUsuario().getId();
        Carteira carteira = carteiraService.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        if (carteira.getSaldo() < dto.valor()) {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }

        carteira.setSaldo(carteira.getSaldo() - dto.valor());
        carteiraService.salvar(carteira);

        Aposta aposta = new Aposta();
        aposta.setEvento(eventoOpt.get());
        aposta.setUsuario(userDetails.getUsuario());
        aposta.setValor(dto.valor());
        aposta.setEscolha(ResultadoEnum.valueOf(dto.escolha()));
        aposta.setRetorno(null);
        aposta.setAcertou(null);

        Aposta salva = apostaService.salvar(aposta);

        transacaoService.criarTransacao(carteira, dto.valor(), TipoTransacao.APOSTA);

        return ResponseEntity.ok(salva);
    }

    @GetMapping
    public ResponseEntity<List<Aposta>> listarApostas(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Aposta> apostas = apostaService.listarPorUsuarioId(userDetails.getUsuario().getId());
        return ResponseEntity.ok(apostas);
    }

    @PostMapping("/simular/{eventoId}")
    @Transactional
    public ResponseEntity<?> simularResultado(@PathVariable Long eventoId) {
        var eventoOpt = eventoService.buscarPorId(eventoId);
        if (eventoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Evento não encontrado");
        }

        Evento evento = eventoOpt.get();
        if (evento.getResultado() != null) {
            return ResponseEntity.badRequest().body("Evento já tem resultado");
        }

        ResultadoEnum[] resultados = ResultadoEnum.values();
        ResultadoEnum resultado = resultados[new Random().nextInt(resultados.length)];
        evento.setResultado(resultado);
        eventoService.salvar(evento);

        var apostas = apostaService.listarPorEventoId(eventoId);
        for (Aposta aposta : apostas) {
            boolean acertou = aposta.getEscolha() == resultado;
            aposta.setAcertou(acertou);

            if (acertou) {
                double odd = switch (resultado) {
                    case TIME_A -> evento.getOddTimeA();
                    case TIME_B -> evento.getOddTimeB();
                    case EMPATE -> evento.getOddEmpate();
                };
                double retorno = aposta.getValor() * odd;
                aposta.setRetorno(retorno);

                Carteira carteira = aposta.getUsuario().getCarteira();
                carteira.setSaldo(carteira.getSaldo() + retorno);
                carteiraService.salvar(carteira);
                transacaoService.criarTransacao(carteira, retorno, TipoTransacao.PREMIO);
            } else {
                aposta.setRetorno(0.0);
            }
            apostaService.salvar(aposta);
        }

        return ResponseEntity.ok(Map.of("resultado", resultado));
    }
}